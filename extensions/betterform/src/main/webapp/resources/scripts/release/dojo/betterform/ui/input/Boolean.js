/*
	Copyright (c) 2004-2009, The Dojo Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/


if(!dojo._hasResource["betterform.ui.input.Boolean"]){dojo._hasResource["betterform.ui.input.Boolean"]=true;dojo.provide("betterform.ui.input.Boolean");dojo.require("betterform.ui.ControlValue");dojo.require("dijit.form.CheckBox");dojo.declare("betterform.ui.input.Boolean",[betterform.ui.ControlValue,dijit.form.CheckBox],{postMixInProperties:function $DA2a_(){this.inherited(arguments);this.applyProperties(dijit.byId(this.xfControlId),this.srcNodeRef);},postCreate:function $DA2b_(){this.inherited(arguments);this.setCurrentValue();},onClick:function $DA2c_(){this.inherited(arguments);this.setControlValue();},_onFocus:function $DA2d_(){this.inherited(arguments);this.handleOnFocus();},_onBlur:function $DA2e_(){this.inherited(arguments);this.handleOnBlur();},_handleSetControlValue:function $DA2f_(_1){if(_1=="true"||_1=="false"){this.attr("checked",eval(_1));}},getControlValue:function $DA2g_(){var _2=this.attr("checked");var _3;if(_2=="true"||_2==true){_3=true;}else{if(_2=="false"||_2==false){_3=false;}}if(_3==undefined){var _4=this.attr("value");if(_4=="true"||_4==true){_3=true;}else{if(_4=="false"||_4==false){_3=false;}}}if(_3!=undefined&&_3){return true;}else{return false;}},setTextValue:function $DA2h_(_5){this.xfControl.setControlValue(this.checked);},getTextValue:function $DA2i_(){return this.checked;}});}