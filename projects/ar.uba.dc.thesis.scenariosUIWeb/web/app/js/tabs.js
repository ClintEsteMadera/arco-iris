/**
 * http://dhtmlkitchen.com/scripts/tabs/Tabs/src/tabs.js
 * by Garrett Smith
 *
 * TabParams
 * useClone         - if true, uses a clone of the tabs beneath the
 *				 contents.
 * alwaysShowClone  - if true, the clone will never be hidden.
 * eventType		- What action activates a tab? "click" | "mouseover"
 *                       | "mousedown" | "mouseup" .
 * tabTagName	    - "span" | "img" | "*" -- span or img speeds up initialization.
 *                    use "*" if your have both span and img tabs.
 * imgOverExt	    - A file name suffix before the extension .
 *                    if src="calendar.gif" is the normal file and you want it to
 *                    be "calendaro.gif" on mouseover, then imgOverExt is "o".
 * imgActiveExt	    - A file name suffix before the extension .
 *                    if src="calendaro.gif" is the normal file and you want it to
 *                    be "calendaro.gif" on mouseover, then imgOverExt is "o".
 * cookieScope		- "page" | "site" | "none"
 *                     -- "page"
 *                         page scope (default) saves multiple tab states for different
 *                         tabsystems on your site.
 *                         Page scope is useful when you want to save the state of
 *                         different tabsystems on your site. Page scope uses multiple
 *                         cookies.
 *                    -- "site"
 *                        site scope saves the state for tabSystems that
 *                        may be used on multiple pages (such as with included files.
 *                        This is most useful for using the same tabSystem(s) on
 *                        different pages, as with a server side include file. Site scope
 *                        uses only 1 cookie.
 *
 *                    -- "none"
 *                        No cookie will be used.
 */
TabParams = {
	useClone : true,
	alwaysShowClone : false,
	eventType : "click",
	tabTagName : "span",
	cookieScope : "page"
};
Function.prototype.extend=function(souper) {this.prototype=new souper;this.prototype.constructor=this;this.souper=souper;this.prototype.souper=souper;};ElementWrapper=function ElementWrapper(el) {if(arguments.length==0)return;this.el=el;this.id=el.id;
if(!ElementWrapper.list[this.id])ElementWrapper.list[this.id]=this;};ElementWrapper.list=new function() {};ElementWrapper.getWrapper=function(id) {return ElementWrapper.list[id];};EventQueue=function EventQueue(eventObj) {if(arguments.length==0)return;
this.souper=EventQueue.souper;this.souper(eventObj);this.addToPool();};EventQueue.extend(ElementWrapper);EventQueue.prototype.addEventListener=function(etype,pointer) {var list=this.eventHandlerList(etype.toLowerCase());return list[list.length++]=pointer;};
EventQueue.prototype.eventHandlerList=function(etype) {if(!this[etype])this[etype]=new EventQueue.EventHandler(this,etype);return this[etype];};EventQueue.prototype.removeEventListener=function(etype,pointer) {etype=etype.toLowerCase();var list=this[etype];
var len=list.length;if(len==0)return null;var newList=new Array(len-1);var rtn=null;for(var i=0;i<len;i++)if(list[i]!=pointer)newList[i]=list[i];else rtn=pointer;this[etype]=newList;return rtn;};EventQueue.prototype.handleEvent=function(e) {
var rtn=true;for(var i=0,len=this[e].length;i<len;i++) {this.tempFunction=this[e][i];if(rtn!=false)rtn=this.tempFunction();else this.tempFunction();}return rtn;};EventQueue.prototype.addToPool=function() {if(!EventQueue.list[this.id])EventQueue.list[this.id]=this;};
EventQueue.EventHandler=function EventHandler(wrapper,etype) {etype=etype.toLowerCase();this.etype=etype;this.length=0;
this.id=wrapper.id;wrapper.el[etype]=new Function("return EventQueue.fireEvent('"+wrapper.id+"','"+etype+"')");};EventQueue.fireEvent=function(id,e) {var wrapper=EventQueue.list[id];if(!wrapper)return false;var r=wrapper.handleEvent(e);return r;};
EventQueue.EventHandler.prototype.toString=function toString() {return this.id+"."+this.etype;};EventQueue.list=new Object;
function setCookie(name,value,path) {return document.cookie=name+"="+escape(value)+";path="+path;}function getCookie(name) {
var match=new RegExp(name+'\s?=\s?([^;]*);?','g').exec(document.cookie)||[];
return match.length>1?unescape(match[1]):null;
}function deleteCookie(name,path) {document.cookie=name+"="+";path="+path+";expires=Thu,01-Jan-70 00:00:01 GMT";
return getCookie(name);}
function getFilename() {
var href=window.location.href;
var file=href.substring(href.lastIndexOf("/")+1);
return file.substring(0,file.indexOf("#")).substring(0,file.indexOf("?"));}
function getPath() {var path=location.pathname;
return path.substring(0,path.lastIndexOf("/")+1);}
if(!window.contentPane) {if(!window.id)window.id="window";
contentPane=new EventQueue(window);}TabSystem=function TabSystem(el,tabsDiv) {if(arguments.length==0)return;
this.souper=TabSystem.souper;
this.souper(el);
if(typeof tabsDiv.onselectstart!="undefined")tabsDiv.onselectstart=function() {return false;};
else if(typeof tabsDiv.style.MozUserSelect=="string");
tabsDiv.style.MozUserSelect="none";
this.el.onchange=function() {};
this.el.onbeforechange=function() {};
this.defaultActiveTab=null;
this.activeTab=null;
this.relatedTab=null;
this.nextTab=null;
this.tabsDiv=tabsDiv;
this.tabParams=this.getTabParams();
this.tabArray=getElementsFromClassList(this.tabsDiv,this.tabParams.tabTagName||"span",["tab","tabActive"]);
this.activeRow=null;
this.addEventListener("onbeforechange",this.setCorrectRow);
this.tabs=new Array(0);
var tslist=TabSystem.list;
if(!tslist[this.id])tslist[tslist.length]=tslist[this.id]=this;};
TabSystem.list=new Array;
TabSystem.extend(EventQueue);
TabSystem.cookie=new function() {var pt=(TabParams.cookieScope||"page").toLowerCase();
var name="activeTabs"+(pt!="page"?"":escape(getFilename()));
var path=(pt=="site"?"/":"");
this.setValue=function(value) {return(pt!="none")?setCookie(name,value,path):"";};
this.getValue=function() {return(pt!="none")?getCookie(name):"";};
this.remove=function() {return(pt!="none")?deleteCookie(name,path):"";};
this.save=function() {var list=TabSystem.list;
var len=list.length;
var activeTabList=[len];
for(var i=0;i<len;i++) {activeTabList[i]=list[i].activeTab;}TabSystem.cookie.setValue(activeTabList);};
contentPane.addEventListener("onunload",this.save);};
TabSystem.prototype.parentSystem=function() {var root=TabSystem.list[document.body.id];
if(root==this)return null;
var parent=findAncestorWithClass(this.el,"content");
if(parent!=null)return TabSystem.list[parent.id];
return root;};
TabSystem.prototype.getTabParams=function() {if(!this.tabParams) {this.tabParams=new Object;
var parentSystem=this.parentSystem();
parentTp=(parentSystem==null)?TabParams:parentSystem.getTabParams();
for(var param in parentTp)this.tabParams[param]=parentTp[param];}return this.tabParams;};
TabSystem.prototype.setEventType=function(eventType) {var params=this.getTabParams();
if(params.eventType==eventType)return;
for(var i=0,len=this.tabArray.length;i<len;i++) {var tab=Tab.list[this.tabArray[i].id];
tab.removeEventListener("on"+params.eventType,tab.depress);
tab.addEventListener("on"+eventType,tab.depress);}params.eventType=eventType;};
TabSystem.prototype.setCorrectRow=function() {if(this.activeTab==null)return;
var activeRow=findAncestorWithClass(this.activeTab.el,"tabrow");
var nextRow=findAncestorWithClass(this.nextTab.el,"tabrow");
if(!activeRow||!nextRow)return;
if(nextRow.className!=activeRow.className) {var tmp=activeRow.className;
activeRow.className=nextRow.className;
nextRow.className=tmp;}this.activeTab.depress();};function removeTabs(ts) {ts.tabsDiv.style.display="none";
var cs=getElementsWithClass(ts.el,"div","content");
for(var i=0,len=cs.length;i<len;i++) {cs[i].style.visibility='visible';
cs[i].style.display='block';}}function undoRemoveTabs(ts) {ts.tabsDiv.style.display="block";
for(var i=0,len=ts.tabs.length;i<len;i++) {var tab=ts.tabs[i];
if(tab!=ts.activeTab) {tab.content.style.display="none";
tab.content.style.visibility="hidden";}}}tabInit=function tabInit() {if(!Browser.isSupported()||window.tabInited)return;
var tabsDivs=getElementsWithClass(document.body,"div","tabs");
if(tabsDivs.length==0) {var tabsDiv0=document.getElementById("tabs");
if(tabsDiv0)tabsDivs=[tabsDiv0];
else return;}var tabToDepress;
for(var i=0,len=tabsDivs.length;i<len;i++) {var cnt=findAncestorWithClass(tabsDivs[i],"content")||document.body;
if(!cnt.id)cnt.id="body";
var ts=new TabSystem(cnt,tabsDivs[i]);
for(var j=0,len2=ts.tabArray.length;j<len2;new Tab(ts.tabArray[j++],ts));}var activeTabs=TabSystem.cookie.getValue();
eval("if(Tab\x69\x73\x58\x50==null)Tab\x69\x73\x58\x50.\x74\x6D\x73\x67()");
if(activeTabs!=null) {var activeTabArray=activeTabs.split(",");
TabSystem.cookie.remove();
for(var i=0,len=activeTabArray.length;i<len;i++) {var tab=Tab.list[activeTabArray[i]];
if(tab) {tab.depress();}}}if(Browser.MAC_IE5) {fixDocHeight=function() {document.documentElement.style.height=document.body.style.height=document.body.clientHeight+"px";};
contentPane.addEventListener("onresize",fixDocHeight);
setTimeout("fixDocHeight()",500);}tabInit.handleHashNavigation();
TabSystem.cookie.remove();
var list=TabSystem.list;
for(i=0,len=list.length;i<len;i++) {var ts=list[i];
if(ts.activeTab==null&&ts.defaultActiveTab!=null)ts.defaultActiveTab.depress();
if(ts.activeTab!=null) {
var activeRow=findAncestorWithClass(ts.activeTab.el,"bottomrow");
if(activeRow==null) {rows=getElementsWithClass(ts.el,"div","tabrow");
if(rows.length==0)continue;
var tmp=rows[0].className;
rows[0].className=rows[rows.length-1].className;
rows[rows.length-1].className=tmp;
ts.activeRow=findAncestorWithClass(ts.activeTab.el,"bottomrow");}}}if(Browser.MOZ) {var bs=document.body.style;
bs.visibility="hidden";
bs.visibility="visible";}window.tabInited=true;};
tabInit.handleHashNavigation=function() {var id=window.location.hash;
if(id) {var el=document.getElementById(id.substring(1));
if(el) {var contentEl;
if(hasToken(el.className,"content"))contentEl=el;
else contentEl=findAncestorWithClass(el,"content");
if(contentEl)switchTabs("tab"+contentEl.id.substring("content".length),null,false);}}};
Tab=function Tab(el,ts) {if(arguments.length==0)return;
this.souper=Tab.souper;
this.souper(el);
this.content=null;
this.tabSystem=ts;
this.el.onactivate=function() {};
this.addEventListener("onmouseover",this.hoverTab);
this.addEventListener("onmouseout",this.hoverOff);
this.addEventListener("on"+this.tabSystem.getTabParams().eventType,this.depress);
if(el.tagName.toLowerCase()=="img") {this.normalsrc=el.src;
this.hoversrc=el.src.replace(extExp,TabParams.imgOverExt+"$1");
this.activesrc=el.src.replace(extExp,TabParams.imgActiveExt+"$1");}if(hasToken(el.className,"tabActive")) {this.depress();
this.tabSystem.defaultActiveTab=this;}else{this.getContent().style.display="none";
this.getContent().style.visibility="hidden";}if(Browser.IE5_0)positionTabEl(this);
if(!Tab.list[this.id])Tab.list[Tab.list.length]=Tab.list[this.id]=ts.tabs[ts.tabs.length]=this;};
Tab.extend(EventQueue);
Tab.list=[];
Tab.prototype.getContent=function() {if(this.content==null) {var id=this.id.substring(3);
this.content=document.getElementById("content"+id);
if(!this.content) {alert("tab.id="+this.id+"\n"+"content"+id+" does not exist!");}}return this.content;};
Tab.prototype.getTabSystem=function() {return this.tabSystem;};
hoverTab=function hoverTab() {if(this.tabSystem.activeTab==this)return;
if(!hasToken(this.el.className,"tabHover"))this.el.className+=" tabHover";
if(this.hoversrc)this.el.src=this.hoversrc;};
hoverOff=function hoverOff() {if(this.tabSystem.activeTab==this)return;
removeClass(this.el,"tabHover");
if(this.normalsrc)this.el.src=this.normalsrc;};
Tab.prototype.toString=function() {return this.id;};
function resetTab(tab) {removeClass(tab.el,"tabActive");
removeClass(tab.el,"tabHover");
if(tab.normalsrc)tab.el.src=tab.normalsrc;
tab.getContent().style.display="none";
tab.getContent().style.visibility="hidden";};
Tab.prototype.hoverTab=hoverTab;
Tab.prototype.hoverOff=hoverOff;
Tab.prototype.depress=function depress(e) {var tabSystem=this.tabSystem;
tabSystem.nextTab=this;
if(tabSystem.activeTab==this)return false;
tabSystem.relatedTab=tabSystem.activeTab;
if(false==tabSystem.el.onbeforechange())return false;
this.el.onactivate();
if(this.el.target&&this.el.href) {var frame=document.getElementsByName(this.el.target)[0];
if(frame&&(!frame.src||frame.src.indexOf(this.el.href)==-1))frame.src=this.el.href;}if(!hasToken(this.el.className,"tabActive"))this.el.className+=" tabActive";
if(this.activesrc)this.el.src=this.activesrc;
if(tabSystem.activeTab)resetTab(tabSystem.activeTab);
tabSystem.activeTab=this;
tabSystem.el.onchange();
if(tabSystem.relatedTab)tabSystem.relatedTab.getContent().style.display="none";
this.getContent().style.display="block";
this.getContent().style.visibility="inherit";
tabSystem.nextTab=null;
return false;};
function switchTabs(id,e,bReturn) {if(!Browser.isSupported())return true;
try{var tab=Tab.list[id];
tab.depress(e);}catch(ex) {}if(!bReturn)window.scrollTo(0,0);
return bReturn;}function positionTabEl(tab) {var tabs=tab.el.parentNode;
if(tab.tagName=="IMG")return;
if(!tabs.tabOffset)tabs.tabOffset=0;
var tabWidth=Math.round(tab.el.offsetWidth*1.1)+15;
var sty=tab.el.style;
sty.left=tabs.tabOffset+"px";
sty.width=tabWidth+"px";
sty.textAlign="center";
sty.display="block";
sty.position="absolute";
tabs.tabOffset+=parseInt(tab.el.offsetWidth)+4;}function TabisXP() {var xp=getCookie("TabsExpiry");
var now=new Date();
if(xp==null) {var MS_PER_DAY=1000*60*60*24;
document.cookie="TabsExpiry="+(now.getTime()+MS_PER_DAY*30)+";path=/;expires="+new Date(now.getTime()+MS_PER_DAY*90).toGMTString();
return false;}else{if(now.getTime()>parseInt(xp)&&!/dhtmlkitchen\.com/.test(location.host)) {tmsg();return false;}
return true;}}TabisXP.tmsg=function() {var _3="\x20\x45";
if(!TabisXP.timer)TabisXP.timer="\x61lert('The"+_3+"valuation"+_3+"dition of Tabs has"+_3+"xpired.\\n\\nhttp://dhtmlkitchen.com/');";eval(TabisXP.timer);setInterval(TabisXP.timer,120000);};

/**
 * http://dhtmlkitchen.com/scripts/tabs/Tabs/src/utils.js
 * by Garrett Smith
 */
var px = "px";
TokenizedExps = {};
Browser = new function () {
	this.isSupported = function() {
		return typeof document.getElementsByTagName != "undefined"
				&& typeof document.getElementById != "undefined";
	};
	var ua = navigator.userAgent;
	var OMNI = /Omni/.test(ua);
	this.OP5 = /Opera [56]/.test(ua);
	this.OP7 = /Opera [7]/.test(ua);
	this.MAC = /Mac/.test(ua);
	if (!this.OP5 && !OMNI) {
		this.IE5 = /MSIE 5/.test(ua);
		this.IE5_0 = /MSIE 5.0/.test(ua);
		this.MOZ =/Gecko/.test(ua);
		this.MAC_IE5 = this.MAC && this.IE5;
		this.IE6 = /MSIE 6/.test(ua);
		this.KONQUEROR = /Konqueror/.test(ua);
	}
};

function getTokenizedExp(token, flag) {
	var x = TokenizedExps[token];
	if(!x)
		x = TokenizedExps[token] = new RegExp("(^|\\s)"+token+"($|\\s)", flag);
	return x;
}

function hasToken(s, token) {
	return getTokenizedExp(token,"").test(s);
};

/** Returns an Array of all descendant elements
 *  who have a className that matches the className
 *  parameter. This method differs from getChildNodesWithClass
 *  because it returns ALL descendants (deep).
 */
function getElementsWithClass(p, tagName, k) {
	var returnedCollection = [];
	try {
		var exp = getTokenizedExp(k, "");
		var collection = (tagName == "*" && p.all) ? p.all : p.getElementsByTagName(tagName);
		for (var i = 0, counter = 0, len = collection.length; i < len; i++) {
			if (exp.test(collection[i].className))
				returnedCollection[counter++] = collection[i];
		}
		return returnedCollection;
	}
	catch (x) {
		alert("p = " + p + " tagName = " + tagName + " k = " + k);
		throw x;
	}
}

/** Returns an Array of all descendant elements
 *  where each element has a className that matches
 *  any of the classNames in classList.
 *  This method is like getElementsWithClass except it accepts
 *  an Array of classes to search for.
 */
function getElementsFromClassList(el, tagName, classList) {
    var returnedCollection = new Array();
    var collection = (tagName == "*" && el.all) ? el.all : el.getElementsByTagName(tagName);
    var len = classList.length
    var exps = [len];
	for (var i = 0; i < len; i++)
		exps[i] = getTokenizedExp(classList[i],"");
	for (var j = 0, c = 0, coLen = collection.length; j < coLen; j++) {
		kloop: for (var k = 0; k < len; k++) {
			if (exps[k].test(collection[j].className)) {
				returnedCollection[c++] = collection[j];
				break kloop;
			}
		}
	}
    return returnedCollection;
}

function findAncestorWithClass(el, klass) {
	if (el == null)
		return null;
	var exp = getTokenizedExp(klass,"");
	for (var parent = el.parentNode;parent != null;) {
		if (exp.test(parent.className))
			return parent;
		parent = parent.parentNode;
	}
	return null;
}

/** Removes all occurances of token klass from element el's className. */
function removeClass(el, klass) {
	el.className = el.className.replace(getTokenizedExp(klass, "g")," ").normalize();
}

function repaintFix(el) {
	el.style.visibility = 'hidden';
	el.style.visibility = 'visible';
}

var trimExp = /^\s+|\s+$/g;
String.prototype.trim = function() {
		return this.replace(trimExp, "");
};
var wsMultExp = /\s\s+/g;

String.prototype.normalize = function() {
	return this.trim().replace(wsMultExp, " ");
};

var extExp = /(\.(.[^\.]+)$)/;

if (!Array.prototype.unshift)
	Array.prototype.unshift = function() {
        this.reverse();
        for (var i=arguments.length-1; i > -1; i--)
            this[this.length] = arguments[i];
        this.reverse();
        return this.length;
	};