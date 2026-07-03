<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 聊天弹窗组件 --%>
<style>
#chatWidget{font-family:-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"PingFang SC","Microsoft YaHei",sans-serif}
#chatWidget *{box-sizing:border-box}
.chat-fab{position:fixed;bottom:24px;right:24px;width:56px;height:56px;background:#00C897;color:#fff;border:none;border-radius:50%;font-size:24px;cursor:pointer;box-shadow:0 4px 16px rgba(0,200,151,.35);z-index:99999;display:flex;align-items:center;justify-content:center;transition:all .2s}
.chat-fab:hover{transform:scale(1.08);box-shadow:0 6px 20px rgba(0,200,151,.45)}
.chat-fab .badge-dot{position:absolute;top:2px;right:2px;width:18px;height:18px;background:#F6465D;color:#fff;border-radius:50%;font-size:11px;display:flex;align-items:center;justify-content:center;font-weight:700}
.chat-panel{position:fixed;bottom:90px;right:24px;width:420px;height:560px;background:#fff;border-radius:16px;box-shadow:0 8px 32px rgba(0,0,0,.15);z-index:99998;display:none;flex-direction:column;overflow:hidden;border:1px solid #e5e7eb}
.chat-panel.open{display:flex}
.chat-header{background:#00C897;color:#fff;padding:12px 16px;display:flex;justify-content:space-between;align-items:center;font-weight:600;font-size:14px}
.chat-header .close-btn{background:none;border:none;color:#fff;font-size:18px;cursor:pointer;padding:0;line-height:1}
/* 项目进度条 */
.chat-progress{background:#f0fdf4;padding:8px 14px;border-bottom:1px solid #e5e7eb;display:none}
.chat-progress.show{display:block}
.progress-bar-wrap{background:#d1fae5;border-radius:6px;height:8px;overflow:hidden;margin-bottom:4px}
.progress-bar-fill{height:100%;background:#00C897;border-radius:6px;transition:width .3s}
.progress-status{font-size:11px;color:#059669;display:flex;justify-content:space-between}
/* Tab切换 */
.chat-tabs{display:none;background:#f9fafb;border-bottom:1px solid #e5e7eb}
.chat-tabs.show{display:flex}
.chat-tab{flex:1;text-align:center;padding:8px 0;font-size:12px;font-weight:600;color:#6b7280;cursor:pointer;border-bottom:2px solid transparent;transition:all .12s}
.chat-tab:hover{color:#111827}
.chat-tab.active{color:#00C897;border-bottom-color:#00C897}
/* 频道列表 */
.chat-channel-list{flex:1;overflow-y:auto;background:#f9fafb}
.chat-channel-list .channel-item{padding:10px 14px;border-bottom:1px solid #e5e7eb;cursor:pointer;transition:background .12s;display:flex;align-items:center;gap:8px}
.chat-channel-list .channel-item:hover{background:#f3f4f6}
.chat-channel-list .channel-item.active{background:#e0f7ec;border-left:3px solid #00C897}
.channel-avatar{width:32px;height:32px;background:#d1fae5;color:#059669;border-radius:50%;display:flex;align-items:center;justify-content:center;font-weight:700;font-size:12px;flex-shrink:0}
.channel-info{flex:1;min-width:0}
.channel-title{font-weight:600;font-size:12px;color:#111827;white-space:nowrap;overflow:hidden;text-overflow:ellipsis}
.channel-preview{font-size:11px;color:#9ca3af;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;margin-top:1px}
.channel-status-badge{font-size:10px;padding:1px 6px;border-radius:4px;flex-shrink:0}
.channel-unread{width:8px;height:8px;background:#F6465D;border-radius:50%;flex-shrink:0}
/* 聊天消息 */
.chat-messages{flex:1;overflow-y:auto;padding:10px 14px;display:none;flex-direction:column}
.chat-messages.active{display:flex}
.chat-msg-row{margin-bottom:8px;display:flex}
.chat-msg-row.self{justify-content:flex-end}
.chat-bubble{max-width:80%;padding:6px 12px;border-radius:12px;font-size:12px;line-height:1.5;word-break:break-word}
.chat-msg-row.self .chat-bubble{background:#00C897;color:#fff;border-bottom-right-radius:3px}
.chat-msg-row.other .chat-bubble{background:#f3f4f6;color:#111827;border-bottom-left-radius:3px}
.chat-msg-time{font-size:10px;color:#9ca3af;margin-top:2px;text-align:right}
.chat-msg-sender{font-size:10px;font-weight:600;color:#6b7280;margin-bottom:2px}
/* 输入 */
.chat-input-area{padding:8px 12px;border-top:1px solid #e5e7eb;display:none;background:#fff;flex-direction:column;gap:6px}
.chat-input-area.active{display:flex}
.chat-input-row{display:flex;gap:6px}
.chat-input-area input{flex:1;border:1px solid #d1d5db;border-radius:18px;padding:6px 12px;font-size:12px;outline:none}
.chat-input-area input:focus{border-color:#00C897}
.chat-input-area .send-btn{background:#00C897;color:#fff;border:none;border-radius:18px;padding:6px 14px;font-size:12px;font-weight:600;cursor:pointer;white-space:nowrap}
.chat-input-area .send-btn:hover{background:#00DBA3}
.chat-action-row{display:flex;gap:6px}
.chat-action-btn{flex:1;padding:5px 0;border-radius:8px;font-size:11px;font-weight:600;cursor:pointer;text-align:center;border:none;transition:all .12s}
.chat-action-btn.upload{background:#d1fae5;color:#059669}
.chat-action-btn.upload:hover{background:#a7f3d0}
.chat-action-btn.complete{background:#fef3c7;color:#d97706}
.chat-action-btn.complete:hover{background:#fde68a}
.chat-action-btn.confirm{background:#dbeafe;color:#2563eb}
.chat-action-btn.confirm:hover{background:#bfdbfe}
.chat-action-btn:disabled{opacity:.5;cursor:not-allowed}
/* 交付列表 */
.chat-deliveries{display:none;flex-direction:column;flex:1;overflow-y:auto;padding:10px 14px}
.chat-deliveries.active{display:flex}
.delivery-item{padding:8px 10px;background:#f9fafb;border-radius:8px;margin-bottom:6px;font-size:12px}
.delivery-item .del-title{font-weight:600;color:#111827}
.delivery-item .del-meta{font-size:10px;color:#6b7280;margin-top:2px}
/* 空状态 */
.chat-empty{flex:1;display:flex;flex-direction:column;align-items:center;justify-content:center;color:#9ca3af;font-size:12px}
.chat-empty .icon{font-size:30px;margin-bottom:6px}
</style>

<div id="chatWidget">
<button class="chat-fab" id="chatFab" onclick="toggleChat()">💬<span class="badge-dot" id="chatUnreadBadge" style="display:none">0</span></button>
<div class="chat-panel" id="chatPanel">
  <div class="chat-header"><span>💬 沟通消息</span><button class="close-btn" onclick="closeChat()">✕</button></div>
  <div class="chat-progress" id="chatProgress"><div class="progress-bar-wrap"><div class="progress-bar-fill" id="progressFill" style="width:0%"></div></div><div class="progress-status"><span id="progressLabel">未开始</span><span id="progressDetail"></span></div></div>
  <div class="chat-tabs" id="chatTabs"><div class="chat-tab active" onclick="switchChatTab('chat')">💬 聊天</div><div class="chat-tab" onclick="switchChatTab('delivery')">📎 交付</div></div>
  <div class="chat-channel-list" id="chatChannelList"></div>
  <div class="chat-messages" id="chatMessages"></div>
  <div class="chat-deliveries" id="chatDeliveries"></div>
  <div class="chat-input-area" id="chatInputArea">
    <div class="chat-action-row" id="chatActionRow"></div>
    <div class="chat-input-row"><input type="text" id="chatInput" placeholder="输入消息..." maxlength="500" onkeydown="if(event.key==='Enter')sendChatMsg()"><button class="send-btn" onclick="sendChatMsg()">发送</button></div>
  </div>
</div>
</div>

<script>
var chatState={open:false,projects:[],activeProjectId:null,messages:{},deliveries:{},tab:'chat'};
function toggleChat(){chatState.open?closeChat():openChat()}
function openChat(){chatState.open=true;document.getElementById('chatPanel').classList.add('open');document.getElementById('chatUnreadBadge').style.display='none';loadChannels()}
function closeChat(){chatState.open=false;chatState.activeProjectId=null;document.getElementById('chatPanel').classList.remove('open');document.getElementById('chatMessages').classList.remove('active');document.getElementById('chatDeliveries').classList.remove('active');document.getElementById('chatInputArea').classList.remove('active');document.getElementById('chatProgress').classList.remove('show');document.getElementById('chatTabs').classList.remove('show');document.getElementById('chatActionRow').innerHTML=''}
function switchChatTab(tab){chatState.tab=tab;document.querySelectorAll('.chat-tab').forEach(function(el){el.classList.toggle('active',el.getAttribute('onclick').indexOf(tab)>=0)});var msgs=document.getElementById('chatMessages');var dels=document.getElementById('chatDeliveries');var input=document.getElementById('chatInputArea');if(tab==='chat'){msgs.classList.add('active');dels.classList.remove('active');input.classList.add('active');document.getElementById('chatActionRow').innerHTML=''}else{msgs.classList.remove('active');dels.classList.add('active');input.classList.remove('active');loadDeliveries()}}
function loadChannels(){var list=document.getElementById('chatChannelList');list.innerHTML='<div style="padding:20px;text-align:center;color:#9ca3af;font-size:12px;">加载中...</div>';var xhr=new XMLHttpRequest();xhr.open('GET','<%= request.getContextPath() %>/api/myChatProjects',true);xhr.onload=function(){if(xhr.status===200){try{var d=JSON.parse(xhr.responseText);if(d.error){list.innerHTML='<div style="padding:20px;text-align:center;color:#9ca3af;font-size:12px;">请先登录</div>';return}chatState.projects=d.projects||[];renderChannels()}catch(e){list.innerHTML='<div style="padding:20px;text-align:center;color:#ef4444;font-size:12px;">数据解析失败</div>'}}else{list.innerHTML='<div style="padding:20px;text-align:center;color:#ef4444;font-size:12px;">加载失败</div>'}};xhr.send()}
function statusBadge(s){if(!s)return'<span class="channel-status-badge" style="background:#e5e7eb;color:#6b7280">未开始</span>';var o={open:'招募中',in_progress:'进行中',awaiting_confirm:'待确认',completed:'已完成',cancelled:'已取消'};var c={open:'#d1fae5;color:#059669',in_progress:'#dbeafe;color:#2563eb',awaiting_confirm:'#fef3c7;color:#d97706',completed:'#d1fae5;color:#059669',cancelled:'#fee2e2;color:#dc2626'};var n=o[s]||s;var bgc=c[s]||'#e5e7eb;color:#6b7280';return'<span class="channel-status-badge" style="background:'+bgc+'">'+n+'</span>'}
function renderChannels(){var list=document.getElementById('chatChannelList');if(chatState.projects.length===0){list.innerHTML='<div class="chat-empty"><div class="icon">💬</div><div>暂无参与的项目</div><div style="font-size:10px;margin-top:4px;">发布项目或中标后可在此沟通</div></div>';return}var html='';for(var i=0;i<chatState.projects.length;i++){var p=chatState.projects[i];var isActive=p.id===chatState.activeProjectId;var fc=(p.title||'P').charAt(0).toUpperCase();var preview=p.lastMsg?(p.lastMsg.senderName+': '+p.lastMsg.content):'暂无消息';if(preview.length>40)preview=preview.substring(0,40)+'...';var orderStatus=(p.order&&p.order.status)||p.status;html+='<div class="channel-item'+(isActive?' active':'')+'" onclick="selectChannel('+p.id+')"><div class="channel-avatar">'+fc+'</div><div class="channel-info"><div class="channel-title">'+escapeHtml(p.title)+'</div><div class="channel-preview">'+escapeHtml(preview)+'</div></div>'+statusBadge(orderStatus)+(p.unread?'<div class="channel-unread"></div>':'')+'</div>'}list.innerHTML=html}
function selectChannel(pid){if(chatState.activeProjectId===pid)return;chatState.activeProjectId=pid;chatState.tab='chat';var items=document.querySelectorAll('#chatChannelList .channel-item');items.forEach(function(el){el.classList.remove('active')});var idx=chatState.projects.findIndex(function(p){return p.id===pid});if(idx>=0&&items[idx])items[idx].classList.add('active');if(chatState.projects[idx]&&chatState.projects[idx].unread)chatState.projects[idx].unread=false;document.querySelectorAll('.chat-tab').forEach(function(el,i){el.classList.toggle('active',i===0)});document.getElementById('chatDeliveries').classList.remove('active');loadMessages(pid);updateProjectStatus(pid)}
function updateProjectStatus(pid){var p=chatState.projects.find(function(x){return x.id===pid});var progress=document.getElementById('chatProgress');var fill=document.getElementById('progressFill');var label=document.getElementById('progressLabel');var detail=document.getElementById('progressDetail');if(!p){progress.classList.remove('show');return}var order=p.order;var pct=0;var statusText='';var detailText='';if(p.order&&(p.order.status==='in_progress'||p.order.status==='awaiting_confirm'||p.order.status==='completed')){progress.classList.add('show');if(p.order.status==='in_progress'){pct=40;statusText='⏳ 项目进行中';detailText='已托管 ¥'+p.order.escrowAmount}else if(p.order.status==='awaiting_confirm'){pct=70;statusText='⏳ 等待雇主确认';detailText='¥'+p.order.escrowAmount+' 待释放'}else if(p.order.status==='completed'){pct=100;statusText='✅ 项目已完成';detailText='已结算 ¥'+p.order.amount}}else if(p.status==='open'){progress.classList.add('show');pct=10;statusText='📋 招募中';detailText='等待竞标'}else if(p.status==='cancelled'){progress.classList.add('show');pct=0;statusText='❌ 已取消';detailText=''}else{progress.classList.remove('show')}fill.style.width=pct+'%';label.textContent=statusText;detail.textContent=detailText;var tabs=document.getElementById('chatTabs');if(order||p.status!=='open')tabs.classList.add('show');else tabs.classList.remove('show');updateActionRow(pid)}
function updateActionRow(pid){var row=document.getElementById('chatActionRow');var p=chatState.projects.find(function(x){return x.id===pid});if(!p||!p.order){row.innerHTML='';return}var order=p.order;var btns=[];if(order.status==='in_progress'&&p.userRole==='freelancer'){btns.push('<button class="chat-action-btn upload">📎 上传交付</button>');btns.push('<button class="chat-action-btn complete">✅ 标记完成</button>');row.innerHTML=btns.join('');row.querySelector('.upload').onclick=function(){uploadDelivery(pid)};row.querySelector('.complete').onclick=function(){markComplete(pid)}}else if(order.status==='in_progress'&&p.userRole==='employer'){btns.push('<span style="font-size:11px;color:#6b7280;padding:2px 0">⏳ 等待自由职业者交付</span>');row.innerHTML=btns.join('')}else if(order.status==='awaiting_confirm'&&p.userRole==='employer'){btns.push('<button class="chat-action-btn confirm">✅ 确认完成并释放资金</button>');row.innerHTML=btns.join('');row.querySelector('.confirm').onclick=function(){confirmComplete(pid)}}else if(order.status==='awaiting_confirm'&&p.userRole==='freelancer'){btns.push('<span style="font-size:11px;color:#d97706;padding:2px 0">⏳ 等待雇主确认</span>');row.innerHTML=btns.join('')}else if(order.status==='completed'){btns.push('<span style="font-size:11px;color:#059669;padding:2px 0">✅ 已完成</span>');row.innerHTML=btns.join('')}else{row.innerHTML=''}}
function loadMessages(pid){var msgArea=document.getElementById('chatMessages');msgArea.innerHTML='<div style="padding:20px;text-align:center;color:#9ca3af;font-size:12px;">加载中...</div>';msgArea.classList.add('active');document.getElementById('chatInputArea').classList.add('active');document.getElementById('chatInput').value='';setTimeout(function(){document.getElementById('chatInput').focus()},100);var xhr=new XMLHttpRequest();xhr.open('GET','<%= request.getContextPath() %>/api/chatMessages?projectId='+pid,true);xhr.onload=function(){if(xhr.status===200){try{var d=JSON.parse(xhr.responseText);chatState.messages[pid]=d.messages||[];renderMessages(pid)}catch(e){msgArea.innerHTML='<div style="padding:20px;text-align:center;color:#ef4444;font-size:12px;">消息加载失败</div>'}}else{msgArea.innerHTML='<div style="padding:20px;text-align:center;color:#ef4444;font-size:12px;">消息加载失败</div>'}};xhr.send()}
function renderMessages(pid){var msgArea=document.getElementById('chatMessages');var msgs=chatState.messages[pid]||[];if(msgs.length===0){msgArea.innerHTML='<div class="chat-empty"><div class="icon">💬</div><div>暂无消息</div></div>';return}var html='';for(var i=0;i<msgs.length;i++){var m=msgs[i];html+='<div class="chat-msg-row '+(m.isSelf?'self':'other')+'"><div class="chat-bubble">'+(m.isSelf?'':'<div class="chat-msg-sender">'+escapeHtml(m.senderName)+'</div>')+'<div>'+escapeHtml(m.content)+'</div><div class="chat-msg-time">'+(m.time||'')+'</div></div></div>'}msgArea.innerHTML=html;msgArea.scrollTop=msgArea.scrollHeight}
function sendChatMsg(){var input=document.getElementById('chatInput');var content=input.value.trim();if(!content||!chatState.activeProjectId){alert('请先选择一个项目频道');input.focus();return}console.log('Sending msg to project',chatState.activeProjectId,':',content);input.disabled=true;var fd=new URLSearchParams();fd.append('projectId',chatState.activeProjectId);fd.append('content',content);var xhr=new XMLHttpRequest();xhr.open('POST','<%= request.getContextPath() %>/api/chatMessages',true);xhr.setRequestHeader('Content-Type','application/x-www-form-urlencoded');xhr.onload=function(){input.disabled=false;try{var d=JSON.parse(xhr.responseText);if(d.success||(d.id>0)||(!d.error)){input.value='';loadMessages(chatState.activeProjectId);loadChannels()}else if(d.error==='not logged in'){alert('请先登录');closeChat()}else if(d.error==='access denied'){alert('无权发送消息')}else{alert('发送失败: '+(d.error||'未知错误'))}}catch(e){if(xhr.status===200){input.value='';loadMessages(chatState.activeProjectId);loadChannels()}else{alert('发送失败')}}};xhr.onerror=function(){input.disabled=false;alert('网络错误')};xhr.send(fd.toString())}
function loadDeliveries(){var area=document.getElementById('chatDeliveries');var pid=chatState.activeProjectId;if(!pid){area.innerHTML='<div class="chat-empty"><div>请先选择一个项目</div></div>';return}area.innerHTML='<div style="padding:20px;text-align:center;color:#9ca3af;font-size:12px;">加载中...</div>';var xhr=new XMLHttpRequest();xhr.open('GET','<%= request.getContextPath() %>/api/chatDelivery?projectId='+pid,true);xhr.onload=function(){if(xhr.status===200){try{var d=JSON.parse(xhr.responseText);chatState.deliveries[pid]=d.deliveries||[];renderDeliveries()}catch(e){area.innerHTML='<div style="padding:20px;text-align:center;color:#ef4444;font-size:12px;">加载失败</div>'}}else{area.innerHTML='<div style="padding:20px;text-align:center;color:#ef4444;font-size:12px;">加载失败</div>'}};xhr.send()}
function renderDeliveries(){var area=document.getElementById('chatDeliveries');var pid=chatState.activeProjectId;var dels=chatState.deliveries[pid]||[];if(dels.length===0){area.innerHTML='<div class="chat-empty"><div class="icon">📎</div><div>暂无交付物</div><div style="font-size:10px;margin-top:4px;">自由职业者上传文件后可在此查看</div></div>';return}var html='<div style="font-size:12px;font-weight:600;margin-bottom:8px;color:#111827">📎 交付清单 ('+dels.length+')</div>';for(var i=0;i<dels.length;i++){var d=dels[i];html+='<div class="delivery-item"><div class="del-title">'+(d.title||d.fileName||'交付物 #'+(i+1))+'</div><div class="del-meta">'+escapeHtml(d.userName)+' · '+(d.time||'')+'</div>'+(d.description?'<div style="font-size:11px;color:#6b7280;margin-top:2px">'+escapeHtml(d.description)+'</div>':'')+'<div style="font-size:10px;color:#059669;margin-top:2px">📁 '+escapeHtml(d.fileName)+'</div></div>'}area.innerHTML=html}
function uploadDelivery(pid){var fn=prompt('输入交付物标题（可选，直接确定可上传文件）:');fn=fn||'交付物';var input=document.createElement('input');input.type='file';input.onchange=function(){var file=input.files[0];if(!file)return;var fd=new FormData();fd.append('projectId',pid);fd.append('action','upload');fd.append('title',fn);fd.append('file',file);var btn=document.querySelector('.chat-action-btn.upload');if(btn){var orig=btn.textContent;btn.textContent='上传中...';btn.disabled=true}var xhr=new XMLHttpRequest();xhr.open('POST','<%= request.getContextPath() %>/api/chatDelivery',true);xhr.onload=function(){if(btn){btn.textContent=orig;btn.disabled=false}if(xhr.status===200){try{var d=JSON.parse(xhr.responseText);if(d.error){alert('上传失败: '+d.error)}else{alert('✅ '+d.fileName+' 上传成功');refreshAll(pid)}}catch(e){alert('上传失败')}}else{alert('上传失败')}};xhr.send(fd)};input.click()}
function markComplete(pid){if(!confirm('确定标记项目已完成？标记后将等待雇主确认。'))return;var fd=new FormData();fd.append('projectId',pid);fd.append('action','complete');var xhr=new XMLHttpRequest();xhr.open('POST','<%= request.getContextPath() %>/api/chatDelivery',true);xhr.onload=function(){if(xhr.status===200){try{var d=JSON.parse(xhr.responseText);if(d.success){alert('✅ 已标记完成，等待雇主确认');refreshAll(pid)}else{alert(d.error||'操作失败')}}catch(e){alert('操作失败')}}else{alert('操作失败')}};xhr.send(fd)}
function confirmComplete(pid){if(!confirm('确认完成后，托管资金将释放到自由职业者钱包。确认吗？'))return;var fd=new FormData();fd.append('projectId',pid);fd.append('action','confirm');var xhr=new XMLHttpRequest();xhr.open('POST','<%= request.getContextPath() %>/api/chatDelivery',true);xhr.onload=function(){if(xhr.status===200){try{var d=JSON.parse(xhr.responseText);if(d.success){alert('✅ 已确认完成，资金已释放！');refreshAll(pid)}else{alert(d.error||'操作失败')}}catch(e){alert('操作失败')}}else{alert('操作失败')}};xhr.send(fd)}
function escapeHtml(s){if(!s)return '';return s.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(/"/g,'&quot;')}
function refreshAll(pid){loadMessages(pid);loadChannels();setTimeout(function(){updateProjectStatus(pid)},300)}
</script>
<script>
setTimeout(function(){var xhr=new XMLHttpRequest();xhr.open('GET','<%= request.getContextPath() %>/api/myChatProjects',true);xhr.onload=function(){if(xhr.status===200){try{var d=JSON.parse(xhr.responseText);if(d.error)return;var c=0;for(var i=0;i<d.projects.length;i++){if(d.projects[i].unread)c++}var b=document.getElementById('chatUnreadBadge');if(c>0){b.textContent=c>99?'99+':c;b.style.display='flex'}}}catch(e){}};xhr.send()},500);
</script>
