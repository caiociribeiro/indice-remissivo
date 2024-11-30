### Baixar o SDK do Javafx

1. Acesse o [site](https://gluonhq.com/products/javafx/)
2. Baixe a versao referente ao seu SO
3. Extrair o arquivo .zip

### Configurar no IntelliJ

1. **Project Structure** > **Libraries**
2. Clique em **+**
3. Navegue ate a pasta lib do SDK do Javafx
4. Selecione todos os .jar e clique em OK

### Adicionar Run Configuration

1. Va em **Edit Configurations**
2. Clique em **Add new** > **Application**
3. Clique em **Modify Options** > **Add VM Options**
4. Em VM Options:

```
module-path "path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml
```

