Ext.define("E4ds.model.AccessLog",
{
  extend : "Ext.data.Model",
  fields : [ {
    name : "userName",
    type : "string"
  }, {
    name : "logIn",
    type : "date",
    dateFormat : "c"
  }, {
    name : "logOut",
    type : "date",
    dateFormat : "c"
  }, {
    name : "duration",
    type : "string"
  }, {
    name : "browser",
    type : "string"
  }, {
    name : "id",
    type : "int"
  } ],
  proxy : {
    type : "direct",
    directFn : "accessLogService.read",
    reader : {
      root : "records"
    }
  }
});