Ext.define('Starter.view.FormPanel', {
	extend: 'Ext.form.Panel',
	itemId: 'formPanel',
	bodyPadding: 10,
	title: 'FORM_LOAD, FORM_POST and SIMPLE',
	
	constructor: function(config) {
        Ext.applyIf(config, {
        	api: {
        		load: formLoadService.getFormData,
        		submit: formSubmitController.handleFormSubmit
        	},
        	paramsAsHash: true
        });
        this.callParent(arguments);
    },
	
	initComponent: function() {
		var me = this;

		Ext.applyIf(me, {
			
			defaults: {
				anchor: '100%'
			},

			items: [ {
				xtype: 'textfield',
				name: 'osName',
				fieldLabel: 'OS Name',				
				allowBlank: false,
			}, {
				xtype: 'textfield',
				name: 'osVersion',
				fieldLabel: 'OS Version',
			}, {
				xtype: 'numberfield',
				name: 'availableProcessors',
				fieldLabel: 'Available Processors',
				anchor: '60%'
			}, {
				xtype: 'filefield',
				name: 'screenshot',
				fieldLabel: 'Screenshot',
			}, {
				xtype: 'textareafield',
				name: 'remarks',
				fieldLabel: 'Remarks'
			} ],

			buttons: [ {
				xtype: 'button',
				action: 'simple',
				text: 'Call SIMPLE method'
			}, {
				xtype: 'button',
				text: 'Call FORM_LOAD method',
				action: 'form_load'
			}, {
				text: 'Submit',
				action: 'submit',
				disabled: true,
				formBind: true
			} ]

		});

		me.callParent(arguments);
	}

});