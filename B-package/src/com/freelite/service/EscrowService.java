package com.freelite.service;

import com.freelite.dao.*;
import com.freelite.model.*;

/**
 * 担保支付服务 — 结算体系核心
 */
public class EscrowService {

    private WalletDao walletDao = new WalletDao();
    private TransactionLogDao logDao = new TransactionLogDao();
    private ProjectDao projectDao = new ProjectDao();

    /**
     * 雇主充值
     */
    public boolean recharge(int userId, double amount, String description) {
        Wallet w = walletDao.getOrCreate(userId);
        double before = w.getBalance();
        if (!walletDao.recharge(userId, amount)) return false;

        TransactionLog log = new TransactionLog();
        log.setUserId(userId);
        log.setType("recharge");
        log.setAmount(amount);
        log.setBalanceBefore(before);
        log.setBalanceAfter(before + amount);
        log.setDescription(description != null ? description : "充值");
        logDao.insert(log);
        return true;
    }

    /**
     * 授标时冻结资金 — 雇主钱包扣款冻结
     * 返回：是否成功（余额不足返回 false）
     */
    public boolean fundEscrow(int projectId, int employerId, double amount) {
        Wallet w = walletDao.getOrCreate(employerId);
        if (w.getBalance() < amount) return false;

        double balanceBefore = w.getBalance();
        double frozenBefore = w.getFrozen();

        if (!walletDao.freeze(employerId, amount)) return false;

        // 更新项目 escrow 状态
        projectDao.updateEscrow(projectId, amount, "funded");

        TransactionLog log = new TransactionLog();
        log.setUserId(employerId);
        log.setType("freeze");
        log.setAmount(amount);
        log.setBalanceBefore(balanceBefore);
        log.setBalanceAfter(balanceBefore - amount);
        log.setFrozenBefore(frozenBefore);
        log.setFrozenAfter(frozenBefore + amount);
        log.setDescription("项目担保托管 #" + projectId);
        logDao.insert(log);
        return true;
    }

    /**
     * 雇主确认完成 → 资金释放到自由职业者
     */
    public boolean releaseToFreelancer(int projectId, int employerId, int freelancerId, double amount) {
        // 释放雇主的冻结
        String beforeReleaseDesc = String.format("项目完成释放 #%d -> 自由职业者 #%d", projectId, freelancerId);

        Wallet employerWallet = walletDao.getOrCreate(employerId);
        double frozenBefore = employerWallet.getFrozen();
        if (!walletDao.release(employerId, amount)) return false;

        // 记录雇主侧流水
        TransactionLog employerLog = new TransactionLog();
        employerLog.setUserId(employerId);
        employerLog.setType("release");
        employerLog.setAmount(-amount);
        employerLog.setFrozenBefore(frozenBefore);
        employerLog.setFrozenAfter(frozenBefore - amount);
        employerLog.setDescription(beforeReleaseDesc);
        logDao.insert(employerLog);

        // 资金入账到自由职业者
        Wallet freelancerWallet = walletDao.getOrCreate(freelancerId);
        double freelancerBefore = freelancerWallet.getBalance();
        if (!walletDao.income(freelancerId, amount)) return false;

        // 记录自由职业者侧流水
        TransactionLog freelancerLog = new TransactionLog();
        freelancerLog.setUserId(freelancerId);
        freelancerLog.setType("income");
        freelancerLog.setAmount(amount);
        freelancerLog.setBalanceBefore(freelancerBefore);
        freelancerLog.setBalanceAfter(freelancerBefore + amount);
        freelancerLog.setDescription("项目收款 #" + projectId);
        logDao.insert(freelancerLog);

        // 更新项目 escrow 状态
        projectDao.updateEscrow(projectId, 0, "released");
        return true;
    }

    /**
     * 退款 — 解冻并退回雇主
     */
    public boolean refundToEmployer(int projectId, int employerId, double amount) {
        Wallet w = walletDao.getOrCreate(employerId);
        double balanceBefore = w.getBalance();
        double frozenBefore = w.getFrozen();

        if (!walletDao.refund(employerId, amount)) return false;

        projectDao.updateEscrow(projectId, 0, "refunded");

        TransactionLog log = new TransactionLog();
        log.setUserId(employerId);
        log.setType("refund");
        log.setAmount(amount);
        log.setBalanceBefore(balanceBefore);
        log.setBalanceAfter(balanceBefore + amount);
        log.setFrozenBefore(frozenBefore);
        log.setFrozenAfter(frozenBefore - amount);
        log.setDescription("项目退款 #" + projectId);
        logDao.insert(log);
        return true;
    }
}
