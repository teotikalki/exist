/*
	Copyright (c) 2004-2009, The Dojo Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/


if(!dojo._hasResource["betterform.ui.common.InlineAlert"]){dojo._hasResource["betterform.ui.common.InlineAlert"]=true;dojo.provide("betterform.ui.common.InlineAlert");dojo.require("dijit._Widget");dojo.declare("betterform.ui.common.InlineAlert",null,{constructor:function $DA0q_(){},handleValid:function $DA0r_(id,_2){if(_2=="xfDisabled"){this._showState(id,"none");}var _3=dijit.byId(id);if(_2=="onFocus"&&_3.getControlValue()==""){this._showState(id,"hint");}else{if((_2=="applyChanges"||_2=="onBlur")&&_3.getControlValue()!=""){this._showState(id,"info");}else{if((_2=="applyChanges"||_2=="onBlur")&&_3.getControlValue()==""){this._showState(id,"none");}}}var _4=dijit.byId(id+"-value");if(dojo.hasClass(_4.domNode,"bfInvalidControl")){dojo.removeClass(_4.domNode,"bfInvalidControl");}},handleInvalid:function $DA0s_(id,_6){var _7=dijit.byId(id);var _8=dojo.byId(id-"alert");if(_8==undefined){return;}if(_6=="init"){return;}if(_6=="submitError"){this._showState(id,"alert");}var _9;if((_7.getControlValue()==undefined||_7.getControlValue()=="")&&!(dojo.hasClass(_7.domNode,"xsdBoolean"))){_9=true;}else{_9=false;}if(_6=="xfDisabled"){this._showState(id,"none");}if(_9){if(_6=="onBlur"){this._showState(id,"none");}else{if(_6=="applyChanges"){this._showState(id,"none");}else{if(_6=="onFocus"){this._showState(id,"hint");}else{if(_6=="applyChanges"){this._showState(id,"hint");}}}}}else{if(_6=="applyChanges"){this._showState(id,"alert");dojo.addClass(_7.controlValue.domNode,"bfInvalidControl");}else{if(_6=="onFocus"){this._showState(id,"alert");}}}},_showState:function $DA0t_(id,_b){if(_b=="alert"){this._display(id,"hint","none");this._display(id,"info","none");this._display(id,"alert","inline");}else{if(_b=="hint"){this._display(id,"alert","none");this._display(id,"info","none");this._display(id,"hint","inline");}else{if(_b=="info"){this._display(id,"alert","none");this._display(id,"hint","none");this._display(id,"info","inline");}else{if(_b=="none"){this._display(id,"alert","none");this._display(id,"hint","none");this._display(id,"info","none");}else{console.warn("State '"+_b+"' for Control "+id+" is unknown");}}}}},_display:function $DA0u_(id,_d,_e){var _f=dojo.byId(id+"-"+_d);if(_f!=undefined&&_f.innerHTML!=""){dojo.style(_f,"display",_e);}else{console.warn(id+"-"+_d+" is not defined for Control "+id);}}});}