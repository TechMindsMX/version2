<table class="table table-striped table-condensed">
  <thead>
    <tr>
      <g:each in="${resultImport.headers}" var="header">
        <th>${header}</th>             
      </g:each>
      <th>RESULTADO</th>
    </tr>
  </thead>
  <tbody>
    <g:each in="${resultImport.data}" var="row" status="i">
      <tr>
        <g:each in="${row.keySet()}" var="data">
          <td>${row."${data}"}</td>
        </g:each>
        <td>${resultImport.results[i]}</td>
      </tr>
    </g:each>
  </tbody>        
</table>
