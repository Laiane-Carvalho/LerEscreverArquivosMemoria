#Lendo e Escrevendo arquivos de texto#


 Estudamos aqui como ler e escrever informações de arquivo texto na memória interna  e externa do aparelho.
  Utilizamos para conseguir de forma ñ complexa:
  
  ######para escrever o conteúdo, usamos as classes (FilesInputStream e FilesOutoutStream) *  
  ###### Para leitura do arquivo da memoria interna usamos (openFileInput(String)) chamando um método que criamos qual recebe como parametro um (FileInputStream)
  ###### Implementamos os metodos para usar o diretório compartilhado  (fetExternalStoragePublicDiretory(String) provindo da classe Environment)
  ###### e para usar o diretorio privado no cartão de memória usamos o metodo (getExternalFilesDir(String))
  ###### Verificamos as permissõs para essas leituras e ecritas, adicionando no arquivo do Manifes as permissoes necessárias e implementamos os métodos de checar se a permissão foi concedida /revogada pelo usuário ou não com os metodos (checkSelfPermission(Activity,String), sholdShowRequestPermissionRationale(Activity, String), requestPermission(Activity, String[]),int) , onRequestPermissionResut(int, String[],int[]) )
  
  
  
  
  Obs: o Doc DCIM é onde normalmente ficam as fotos e os vídeos no aparelho, utilizando a constante DIRETORY_DCIM, estamos usando a raiz deste diretório, mas podemos utilizar alguns subdiretórios como: DIRETORY_PODCASTS, DIRETORY_RINGTONES, DIRETORY_ALARMS, DIRETORY_NOTIFICATION, DIRETORY_PICTURES ,DIRETORY_MOVIES ,DIRETORY_DOWNLOADS.
  ( Para usar o diretorio privado no cartão de memória podemos passar como parâmrtro no metodo( getExternalFileDir() ) qualquer um desses diretóios acima menos o DCIM ou null...)