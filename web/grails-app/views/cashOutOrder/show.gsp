<%! import com.modulus.uno.CashOutOrderStatus %>
<%! import com.modulus.uno.RejectReason %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'cashOutOrder.label', default: 'CashOutOrder')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-bank fa-3x"></i>
        <g:message code="cashOutOrder.show"/>
      </h1>
    </div>
    <div class="col-lg-12">
      <div class="portlet portlet-blue">
        <div class="portlet-heading">
          <div class="portlet-title">
            <br /><br />
          </div>
          <div class="clearfix"></div>
        </div>
        <div id="defaultPortlet" class="panel-collapse collapse in">
          <div class="portlet-body">
           <g:if test="${flash.message}">
              <div class="alert alert-danger" role="alert">${flash.message}</div>
           </g:if>
           <form class="form-horizontal" role="form">
              <f:display bean="cashOutOrder" property="id"  wrapper="describe"/>
              <f:display bean="cashOutOrder" property="amount"  wrapper="describePrice"/>
              <f:display bean="cashOutOrder" property="account"  wrapper="describe"/>
              <f:display bean="cashOutOrder" property="company"  wrapper="describe"/>
              <div class="form-group">
                <label id="status-label" class="col-sm-5 control-label"><g:message code="cashOutOrder.status" /></label>
                <div class="col-sm-4"><g:message code="cashOutOrder.status.${cashOutOrder.status}"/></div>
              </div>
           </form>

              <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_OPERATOR_EJECUTOR">
                <g:if test="${cashOutOrder.status == CashOutOrderStatus.CANCELED || cashOutOrder.status == CashOutOrderStatus.REJECTED}">
                  <div class="alert alert-danger" role="alert">
                    <label class="control-label">Motivo ${message(code:'cashOutOrder.rejectReason.'+cashOutOrder.status)}:</label>
                  <g:message code="rejectReason.${cashOutOrder.rejectReason}" default="${cashOutOrder.rejectReason}" />
                  <g:if test="${cashOutOrder.comments}">
                  <textarea class="form-control" rows="3" cols="60" readonly>${cashOutOrder.comments}</textarea>
                  </div>
                  </g:if>
                </g:if>
             </sec:ifAnyGranted>

            <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_OPERATOR_EJECUTOR">
            <g:if test="${cashOutOrder.status == CashOutOrderStatus.CREATED}">
            <g:link controller="cashOutOrder" action="sendToAuthorize" class="btn btn-success" id="${cashOutOrder.id}">Confirmar Orden de Retiro</g:link>

            <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#modalConfirm">
              <i class="fa fa-trash"></i> Borrar
            </button>
            <div class="modal fade" id="modalConfirm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
              <div class="modal-dialog" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">Confirme la acción</h4>
                  </div>
                  <div class="modal-body">
                    ¿Está seguro de eliminar la orden seleccionada?
                  </div>
                  <div class="modal-footer">
                    <g:link action="deleteOrder" id="${cashOutOrder.id}" class="btn btn-primary">
                    Sí
                    </g:link>
                    <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                  </div>
                </div>
              </div>
            </div>
            </g:if>
            </sec:ifAnyGranted>

            <sec:ifAnyGranted roles="ROLE_AUTHORIZER_EJECUTOR">
            <g:if test="${cashOutOrder.status == CashOutOrderStatus.TO_AUTHORIZED &&
                  !cashOutOrder.authorizations?.find{it.user == user}}">
            <div class="container-fluid">
              <div class="row">
                <div class="col-md-6">
                  <g:link action="authorizeCashOutOrder" class="btn btn-warning btn-block" id="${cashOutOrder.id}">Autorizar la Orden de Retiro</g:link>
                </div>
                <div class="col-md-6">
                  <a data-toggle="collapse" role="button" href="#inputReasonCancellation" class="btn btn-danger btn-block" aria-expanded="false" aria-controls="inputReasonCancellation">Cancelar la Orden de Retiro</a>
                </div>
                <div class="row">
                  <div class="col-md-12">
                    <div class="collapse" id="inputReasonCancellation">
                      <div class="well">
                        <g:form action="cancelCashOutOrder" id="${cashOutOrder.id}">
                        <div class="form-group">
                          <g:select name="rejectReason" from="${RejectReason.values()}" value="${cashOutOrder.rejectReason}" class="form-control" />
                          <br/>
                          <g:textArea name="comments" placeholder="Comentarios opcionales" rows="3" cols="60" maxLength="255" class="form-control"/>
                          <br/>
                          <button type="submit" class="btn btn-danger">Ejecutar Cancelación</button>
                        </div>
                          </g:form>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <br/>
            </g:if>
            <g:else>
              <g:if test="${cashOutOrder.authorizations?.find{it.user == user}}">
                <div class="alert alert-warning"><g:message code="order.already.authorized"/></div>
              </g:if>
            </g:else>
            </sec:ifAnyGranted>

           <g:if test="${cashOutOrder.status == CashOutOrderStatus.AUTHORIZED}">
             <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR">
             <div class="container-fluid">
               <div class="row">
                 <div class="col-md-6">
                   <g:if test="${enabledToPay}">
                   <g:link action="executeCashOutOrder" class="btn btn-success btn-block" id="${cashOutOrder.id}">Ejecutar orden de Retiro</g:link>
                   </g:if><g:else>
                    <div class="alert alert-warning">
                      La empresa no está habilitada para hacer pagos, verifique que tiene configurada la comisión correspondiente y que se encuentre registrada su cuenta STP
                    </div>
                   </g:else>
                 </div>
                 <div class="col-md-6">
                   <a data-toggle="collapse" role="button" href="#inputReasonCancellation" class="btn btn-danger btn-block" aria-expanded="false" aria-controls="inputReasonCancellation">Rechazar la orden de retiro</a>
                 </div>
                 <div class="row">
                   <div class="col-md-12">
                     <div class="collapse" id="inputReasonCancellation">
                       <div class="well">
                         <g:form action="rejectCashOutOrder" id="${cashOutOrder.id}">
                         <div class="form-group">
                           <g:select name="rejectReason" from="${RejectReason.values()}" value="${cashOutOrder.rejectReason}" class="form-control" />
                           <br/>
                           <g:textArea name="comments" placeholder="Comentarios opcionales" rows="3" cols="60" maxLength="255" class="form-control"/>
                           <br/>
                           <button type="submit" class="btn btn-danger">Rechazar</button>
                         </div>
                           </g:form>
                       </div>
                     </div>
                   </div>
                 </div>
               </div>
             </div>
             </sec:ifAnyGranted>
             </g:if>
          </div>
        </div>
        <div class="portlet-footer">
          <g:if test="${cashOutOrder.status == CashOutOrderStatus.CREATED}">
          <g:link class="edit btn btn-default" action="edit" resource="${this.cashOutOrder}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
          </g:if>


          <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_VISOR,ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_OPERATOR_VISOR,ROLE_OPERATOR_EJECUTOR">
            <g:link class="btn btn-default" action="list"><g:message code="cashoutOrder.list.label" /></g:link>
          </sec:ifAnyGranted>
          <sec:ifAnyGranted roles="ROLE_AUTHORIZER_EJECUTOR,ROLE_AUTHORIZER_VISOR">
            <g:link class="btn btn-default" action="list" params="[status:'TO_AUTHORIZED']"><g:message code="cashoutOrder.list.label" /></g:link>
          </sec:ifAnyGranted>

        </div>
      </div>
    </div>
  </body>
</html>
