<!-- BEGIN TABLE-RESPONSIVE -->
<div class="table-responsive" >
  <!-- BEGIN TABLE -->
  <table class="table table-bordered">
    <thead>
      <tr>
        <th>Estado Inicial</th>
        <th>Acción</th>
        <th>Estado Final</th>
        <th></th>
      </tr>
    </thead>  
    <tbody>
      {{#each transitions}}
        <input type="hidden" name="transitions[{{@index}}].stateFrom" value="{{stateFrom.name}}" />
        <input type="hidden" name="transitions[{{@index}}].stateTo" value="{{stateTo.name}}" />
        {{#each this.actions}}
        <tr>
          <td class="state-from-column">
            {{../stateFrom.name}}
          </td>
          <td class="action-column">
            {{this}}
            <input type="hidden" name="transitions[{{@../index}}].actions[{{@index}}]" value="{{this}}" />
          </td>
          <td class="state-to-column">
            {{../stateTo.name}}
          </td>
          <td>
            <button type="button" class="btn btn-red delete-transition">
              <i class="fa fa-times" aria-hidden="true"></i>
            </button>
          </td>
        </tr>
        {{/each}}
      {{/each}}
    </tbody>
  </table>
  <input type="hidden" name="initialState" value="{{initialState.name}}" />
</div>
<!-- END OF TABLE-RESPONSIVE -->
