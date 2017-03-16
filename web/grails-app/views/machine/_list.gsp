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
        <th>{{uuid}}</th>
        <th>{{dateCreated}}</th>
        <th>
          <g:link class="btn btn-primary" controller="machine" action="show">Ver</g:link>
        </th>
      </tr>
      {{/each}}
    </tbody>
  </table>
{{/if}}
</script>
