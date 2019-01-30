<label>Archivo .key</label>
<input type="file" required="true" class="form-control" name="key" />
<label>Archivo .cer</label>
<input type="file" required="true" class="form-control" name="cer" />
<label>Logotipo <small>(Solo se acepta archivos *.png con dimensiones 254 × 101)</small></label>
<input type="file" required="true" class="form-control" name="logo" accept="image/png" />
<label>Password</label>
<input type="password" required="true" class="form-control" name="password" />
<label>Series de Facturas</label>
<input type="text" class="form-control" name="serieIncomes" placeholder="Serie para Ingresos" /><br/>
<input type="text" class="form-control" name="serieExpenses" placeholder="Serie para Egresos" />
<br />
<select name="pdfTemplate" class="form-control" required="required">
  <option value=""> Seleccione la plantilla PDF...</option>
</select> 
<br/> 
<sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR">
  <input type="submit" class="btn btn-green btn-lg" value="Guardar" />
</sec:ifAnyGranted>

