$("input[name='clientProviderType']").change(function(){
    $("#entityType").val($("input:radio[name='clientProviderType']:checked").val());
  }
);
