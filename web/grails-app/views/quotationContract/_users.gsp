<style>
        th {
          text-align:center;
        }
        
      th, td {
        white-space: nowrap;
        width: 1px;
      }
    
        .fixwidth {
          width: 300px;
        }
    
    </style>
    
    <div class="portlet portlet-default">
      <div class="portlet-heading">
        <div class="portlet-title">
          <h4>Usuarios disponibles para agregar</h4>
        </div>
        <div class="clearfix"></div>
      </div>
    
      <div class="portlet-body">
    <g:hiddenField id="entities" name="entities" value=""/>
        <div class="table-responsive">
                <div class="container-fluid">
          <table class="table table-striped table-condensed">
            <tr>
              <th  style="width:15%; text-align:left"><g:checkBox id="selectAll" name="selectAll" title="Seleccionar Todos"/>   Seleccionar todos</th>
              <th  style="width:85%"> Nombre</th>
            </tr>
    
            <g:each in="${users}" var="user" status="index">
            <tr>
              <td><g:checkBox class="entity" id="checkBe" name="checkBe" value="${user.id}" checked="false"/></td>
              <td>${user.username}</td>
            </tr>
            </g:each>
          </table>
                </div>
        </div>
      </div>
    
      <div class="portlet-footer">
        <button class="btn btn-primary text-right" id="add" type="submit">Agregar</button>
      </div>
    </div>
    
    