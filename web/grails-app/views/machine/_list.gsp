<script id="machine-list-template" type="text/x-handlebars-template">
{{#if machines}}
  <table>
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
        <th></th>
      </tr>
      {{/each}}
    </tbody>
  </table>
{{/if}}
</script>
