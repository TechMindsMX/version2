<script id="machine-list-template" type="text/x-handlebars-template">
{{#if machines}}
  <table class="table">
    <thead>
      <tr>
        <th>Uuid</th>
        <th>Fecha de Creación</th>
        <th></th>
      </tr>
    </thead>
    <tbody>
      {{#each machines}}
      <tr>
        <td>{{uuid}}</th>
        <td>{{dateCreated}}</th>
        <td>
          <a class="btn btn-primary" href="${createLink(controller:'machine',action:'edit')}/{{uuid}}">Agregar</a>
        </td>
      </tr>
      {{/each}}
    </tbody>
  </table>
{{else}}
<div class="alert alert-info">
  No hay máquinas registradas aún.
</div>
{{/if}}
</script>
