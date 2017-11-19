<!DOCTYPE html>
<html>

<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'quotationContract.label', default: 'QuotationContract')}" />
    <title>
        <g:message code="default.create.label" args="[entityName]" />
    </title>
</head>

<body>
    <div class="page-title">
        <h1>
            <i class="fa fa-list-alt fa-3x"></i>
            Ingesos por commsiones
            <small>${company}</small>
        </h1>
    </div>

    <div id="edit-address" class="content scaffold-edit" role="main">
        <div class="portlet portlet-blue">
            <div class="portlet-heading">
                <div class="portlet-title"></div>
                <div class="clearfix"></div>
            </div>

            <div id="horizontalFormExample" class="panel-collapse collapse in">
                <div class="portlet-body">
                    <g:if test="${flash.message}">
                        <div class="message" role="status">${flash.message}</div>
                    </g:if>

                    <g:form action="feeIncome">
                        <label>
                            <g:message code="Periodo" />
                        </label>
                        <div class="row">
                            <div class="col-md-4">
                                <label>
                                    <g:message code="Del:" />
                                </label>
                                <input class="form-control" type="text" id="datepicker" name="initDate" required="required">
                            </div>
                            <div class="col-md-4">
                                <label>
                                    <g:message code="Al:" />
                                </label>
                                <input class="form-control" type="text" id="datepicker1" name="lastDate" required="required">
                            </div>
                            <div class="col-md-4 text-center">
                                <g:submitButton name="consultar" class="btn btn-primary" value="${message(code: 'default.button.consultar.label', default: 'Consultar')}" style="margin-top:25px"
                                />
                            </div>
                        </div>
                        <br>
                        <br>
                    </g:form>

                    <div class="row">
                        <div class="col-md-12">
                            <div class="table-responsive">
                                <table class="table table-striped table-condensed">
                                    <tr>
                                        <th>Cliente</th>
                                        <th>Monto</th>
                                        <th>Comisión</th>
                                    </tr>
                                    <g:each in="${feeIncomes}" var="feeIncome">
                                        <tr>
                                            <td>${feeIncome.client}</td>
                                            <td>
                                                <g:formatNumber number="${feeIncome.amount}" type="currency" currencyCode="MXN"
                                                />
                                            </td>
                                            <td>
                                                ${feeIncome.commission}
                                            </td>
                                        </tr>
                                    </g:each>
                                    <tr>
                                        <th>Total</th>
                                        <th>
                                            <g:formatNumber number="${feeIncomes.sum { it.amount }}" type="currency" currencyCode="MXN"
                                            />
                                        </th>
                                        <th>
                                            <g:formatNumber number="${feeIncomes.sum { it.commission }}" type="currency" currencyCode="MXN"
                                            />
                                        </th>
                                    </tr>
                                </table>

                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
    <asset:javascript src="quotationContract/create.js" />
</body>
</html>