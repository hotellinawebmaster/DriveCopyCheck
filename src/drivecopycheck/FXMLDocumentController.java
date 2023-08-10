// Aldo Marsilio copyrighted 2015
// please send all the bugs to hotellinaweb@gmail.com
// copying this source file is strictly forbidden 


package drivecopycheck;

import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.io.DataOutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
 
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import javafx.concurrent.Worker.State;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONObject;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
//import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.*;
 
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import javafx.application.Platform;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;


import javafx.event.EventHandler;
 
import javafx.scene.web.WebView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.AnchorPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import netscape.javascript.JSObject;
import org.json.JSONException;
import org.w3c.dom.Document;  // this is to scan elements to fetch the email address
import org.w3c.dom.Node; // this is to scan elements to fetch the email address
import org.xml.sax.SAXException;
 
 

/**
 * FXML Controller class
 *
 * @author Aldo Marx
 */

public class FXMLDocumentController implements Initializable {
    
    
    @FXML
    private Button save_btn;

    @FXML
    private PasswordField client_secret_PasswoedField;

    @FXML
    private TextField client_id_txtField;
     
    @FXML
    private ImageView imageview;
    
    @FXML
    private AnchorPane anchorPane;
   
    @FXML
    private WebView webView;
    
    @FXML
    void save_Button_Displayed(KeyEvent event) {
         save_btn.setText("Save now");
         save_btn.setDisable(false);
    }
    
    @FXML
    void saveBtnHAndler(MouseEvent event) {
        writeCredentialsToFile();
    }
    private String access_type="offline";
    
    // https://console.developers.google.com/apis/credentials
    
    private String client_id; 
    private String client_secret; 
    private String fileNameCredentials = "config.properties";
    
    public WebEngine webEngine;
    private String code=null;
    private CookieManager cookieMan;
    private String urlToGo=null;
    private final String USER_AGENT = "Mozilla/5.0";
   
    public String redirect_uri="urn:ietf:wg:oauth:2.0:oob";
    private String refreshToken;
    private String accessToken;
    private boolean managedEmailAddress;
    private static final Charset CHARSET = Charset.forName("UTF-8");
    private Stage mainStage;
   
    
    
    
    private List<com.google.api.services.drive.model.File> currentCloudDriveObject;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
          System.out.println("Initialize called from FXMLDocumentController.java----------------------------------------"); 
        try {
            readCredentialsFromFile();
            getFirstPrompt();
            webEngine.getLoadWorker().stateProperty().addListener(loadedPageHandler);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
     
     
    
   private boolean emailAddressIsManaged(String emailAddressLogin) throws Exception {
           // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            // if email address is in the archive and there is a refresh token then 
            // check and try to get a new access token 
            // if I can get a new access token then return true
            // otherwise return false
           refreshToken= readProfileArchiveandSearchfor(emailAddressLogin);
           System.out.println( "Refreshtoken from Managed=" + refreshToken);
           if ( refreshToken  != null )  return true; 
           else  return false; 
        }
       
       
       private static String readProfileArchiveandSearchfor( String emailAddresstoLokfor) { 
           File file= null;
           boolean fileDidNotExist = false;
           do
           {
                try {
                    File userDir = new File(System.getProperty("user.dir"));
                    file = new File(userDir , "managesemailaddresses.txt"); 
                    Scanner scanner = new Scanner(file);
                    fileDidNotExist = false;
                 while (scanner.hasNextLine()) 
                 {
                     String thisLine = scanner.nextLine();
                    //System.out.println("Scanned email addresses @@@@" + thisLine);
                     if  ( thisLine.split(",")[0].equals(emailAddresstoLokfor )) 
                     { 
                         // System.out.println( "readArchiveandSearchfor++I am returning " + thisLine.split(",")[1]);
                         return  thisLine.split(",")[1];
                     } //returns only the refresh token 
                 }
                    scanner.close();
                    //  System.out.println( "scanner closed sucessfully");
                } catch (FileNotFoundException e) {
                     e.printStackTrace();
                     System.out.println( "File  managesemailaddresses.txt not found");
                      createFileProfilefromZero(file);
                      fileDidNotExist = true;
                     return null;
                } 
               // System.out.println( "readArchiveandSearchfor++I am returning  null ");
           } while (fileDidNotExist);
               
                return null;
 }
    
 private static void createFileProfilefromZero(File aFile)  {
          try {
                    if ( aFile.createNewFile() )
                    {
                        System.out.println("Success while creating the file managesemailaddresses.txt");
                    } else
                    {
                        System.out.println("Failure while creating the file managesemailaddresses.txt");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
             }
 }
       
       
       
  // load elements in another thread.     
  Task<Void> loadElementsWithAccessToken = new Task<Void>() 
{
      @Override
      protected Void call() throws Exception 
      {
                getDriveServiceandLoadData(accessToken );
                return null;
       }
};
       
       
    private ChangeListener<State> loadedPageHandler= new ChangeListener<State>() 
     {       
        @Override
        public void changed(ObservableValue ov, State oldState, State newState) 
        {
           // System.out.println("changed +++called"   );
            if (newState == State.SUCCEEDED) 
            {
                JSObject jsobj = (JSObject) webEngine.executeScript("window");
                jsobj.setMember("java", new HTMLCommunication());
                System.out.println("webEngine.getTitle()=" + webEngine.getTitle()); 
                System.out.println("newState=" + newState);
                 if ( webEngine.getTitle() != null ) 
                // ths event fires also times when webEngine.getTitle() is null
                // and in oirder to avoid an exception thrown
                // I need to filter the running of this with an if 
                 {    
                     if ( webEngine.getTitle().contains("Sign in - Google Accounts") ) 
                     {
                              // webEngine.executeScript("document.getElementById('Email').value =\"hotellinaweb@gmail.com\";"); // debug
                               // this above inject a javascript to fill in the value hotellina@15.novtest.info for faster troubleshooting
                               webEngine.executeScript("document.getElementById(\"signIn\").addEventListener(\"click\", function(){ java.attachOnClickHandler();  }) ");
                     }
                      if ( webEngine.getTitle().contains("Request for Permission") ) 
                      { 
                           System.out.println("--------------------Request for Permission detected---------------------------");
                           if ( managedEmailAddress ) 
                               try 
                               {
                                    System.out.println("...getting accessToken from Managed RefreshToken");
                                    accessToken = getNextAccessToken(refreshToken);
                                    new Thread(loadElementsWithAccessToken).start(); 
                                   loadElementsWithAccessToken.setOnSucceeded(new EventHandler<WorkerStateEvent>() 
                                   {
                                        @Override
                                        public void handle(WorkerStateEvent event) {
                                                imageview.setVisible(false);
                                                switchToDriveShowUp(); 
                                        }
                                    });
                                    
                                } catch (Exception ex) 
                                {
                                   Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                                   deleteProfileForEmailAddress(Drivecopycheck.emailAddressLogin);
                                    managedEmailAddress = false;
                                   webView.setVisible(true);
                                  
                            }
                      }    
                      
                      if ( webEngine.getTitle().contains("Success code=") ) 
                      { 
                         try {
                             code = webEngine.getTitle().substring(13);
                            // System.out.println("Code is valid:" + code);
                             webEngine.getLoadWorker().stateProperty().removeListener(loadedPageHandler); 
                             anchorPane.getChildren().remove(webView);
                             refreshToken = getFirstRefreshToken();
                             storeNewEmailAddressProfile(Drivecopycheck.emailAddressLogin);
                             accessToken = getNextAccessToken(refreshToken); 
                             new Thread(loadElementsWithAccessToken).start();
                             loadElementsWithAccessToken.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                                    @Override
                                    public void handle(WorkerStateEvent event) { 
                                            imageview.setVisible(false);
                                            switchToDriveShowUp(); 
                                    }
                                });
                        
                         } catch (Exception ex) {
                             Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                         }
                      }
                      else   
                      {
                         System.out.println("Page without code" );
                         if (webEngine.getTitle().contains("access_denied")) {
                         deleteCookies();
                         getFirstPrompt();
                        } 
                      }
                 } 
            }
    }

  private void storeNewEmailAddressProfile(String emailAddressLogin) throws IOException {
           // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    List<String> lines = Arrays.asList(emailAddressLogin + "," + refreshToken);
                    Path file = Paths.get("managesemailaddresses.txt");
                    java.nio.file.Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
        }
    }; 
 
private void deleteProfileForEmailAddress(String anEmailAddress)   
{
            try {
                         File inputFile = new File("managesemailaddresses.txt");
                         File tempFile = new File("tempFile.txt");

                         BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                         BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                         String lineToRemove = anEmailAddress;
                         String currentLine;
                         while((currentLine = reader.readLine()) != null) {
                             // trim newline when comparing with lineToRemove
                             String trimmedLine = currentLine.trim();
                             if(trimmedLine.contains(lineToRemove)) continue;
                             writer.write(currentLine + System.getProperty("line.separator"));
                         }
                         writer.close(); 
                         reader.close(); 
                         boolean successful = tempFile.renameTo(inputFile);
                         System.out.println("successful?=" + successful);            
            } catch (IOException ex1) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex1);
            }
     
 }
     
      
     
   private void getFirstPrompt(){
        System.out.println("getFirstPrompt++++Called");
        cookieMan=new CookieManager(null /*=default in-memory store*/, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieMan);
        webEngine= webView.getEngine();    
        urlToGo = "https://accounts.google.com/o/oauth2/auth?access_type=" + access_type + 
                   "&approval_prompt=auto&client_id=" + client_id + 
                   "&redirect_uri=urn:ietf:wg:oauth:2.0:oob&response_type=code&" + 
                   "scope=https://www.googleapis.com/auth/drive https://www.googleapis.com/auth/drive.appdata https://www.googleapis.com/auth/drive.apps.readonly https://www.googleapis.com/auth/drive.file https://www.googleapis.com/auth/drive.metadata https://www.googleapis.com/auth/drive.metadata.readonly https://www.googleapis.com/auth/drive.readonly";
        webEngine.load(urlToGo);  
    }
          
    
  // retrieve all files from Drive via API
   private  List<com.google.api.services.drive.model.File> retrieveAllFiles(Drive service) throws IOException {
    List<com.google.api.services.drive.model.File> result = new ArrayList<com.google.api.services.drive.model.File>();
    Files.List request = service.files().list().setQ("'" + Drivecopycheck.emailAddressLogin + "' in owners and trashed = false"); 
    do {
      try {
        System.out.println("Tryng to speak to Google to fetch files before exec"  ); 
        FileList files = request.execute();
         //System.out.println("Tryng to speak to Google to fetch files after exec" + files. ); 
        result.addAll(files.getItems());
        request.setPageToken(files.getNextPageToken());
      } catch (IOException e) {
        System.out.println("An error occurred: " + e);
        request.setPageToken(null);
      }
    } while (request.getPageToken() != null &&  request.getPageToken().length() > 0);
    System.out.println("#of elements =========" + result.size()); 
    return result;
  }
   
    
   // the getNextAccessToken below gets an access token from a refresh token
   private String getNextAccessToken(String refreshTokenPar) throws Exception {
      
        String url = "https://www.googleapis.com/oauth2/v4/token";
        URL URLobj = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) URLobj.openConnection();

        //add reuqest header
        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters =  "client_id="      +client_id +"&"+
                               "client_secret="  +client_secret +"&"+
                               "refresh_token="+refreshTokenPar+"&"+
                               "grant_type="     +"refresh_token" ;

        // Send post request
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = connection.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
        }
        in.close();

       JSONObject jsonObject = new JSONObject(response.toString());
       //System.out.println(jsonObject);

       //refresh_token = jsonObject.getString("refresh_token");
       //access_token =  jsonObject.getString("access_token");       
       //System.out.println(refresh_token);
      // System.out.println(access_token);
       return  jsonObject.getString("access_token");       
  }
   
   
   
   /* this is the first operation with Google
   
   */
   private String getFirstRefreshToken()   {
      
       DataOutputStream wr = null;
        JSONObject jsonObject = null;
        try {
            String url = "https://www.googleapis.com/oauth2/v4/token";
            URL URLobj = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) URLobj.openConnection();
            //add reuqest header
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            String urlParameters = "code="           +code +"&"+
                    "client_id="      +client_id +"&"+
                    "client_secret="  +client_secret +"&"+
                    "redirect_uri="   +redirect_uri +"&"+
                    "access_type="    +access_type +"&"+
                    "grant_type="     +"authorization_code" ;
            // Send post request
            connection.setDoOutput(true);
            wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            int responseCode = connection.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }   in.close();
            jsonObject = new JSONObject(response.toString());
            // System.out.println(jsonObject);
            
        } 
        catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (JSONException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally {
            try {
                wr.close();
                 return  jsonObject.getString("refresh_token");
                
            } catch (Exception ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
          return null;
  }
  
   
   
   
   private void getDriveServiceandLoadData(String anAccess_token) throws IOException {
         
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        GoogleCredential credential = new GoogleCredential.Builder()
                                                                    .setTransport(httpTransport)
                                                                    .setJsonFactory(jsonFactory)
                                                                    .setClientSecrets(client_id, client_secret)
                                                                    .build();

        credential.setAccessToken(anAccess_token);

        // Create a new authorized API client
        Drivecopycheck.service = new Drive.Builder(httpTransport, jsonFactory, credential)
                                                            .setApplicationName("DriveCopyChecker")
                                                            .build();
        Drivecopycheck.globalRepoList  = retrieveAllFiles(Drivecopycheck.service);
        currentCloudDriveObject =Drivecopycheck.globalRepoList; // puts all the content in this instance class variable globalRepoList
      
        
//                 //Insert a file  
//                File body = new File();
//                body.setTitle("document.txt");
//                body.setDescription("A test document");
//                body.setMimeType("text/plain");
//
//                java.io.File fileContent = new java.io.File("src/treeviewproject/document.txt");
////                
//                FileContent mediaContent = new FileContent("text/plain", fileContent);
////
//                File file = service.files().insert(body, mediaContent).execute();
//                System.out.println("File ID: " + file.getId());
//                
//                DataInputStream reader = new DataInputStream(System.in);
//                System.out.print("Do you want to delete this file? Y/N ");
//                
//                try {
//                      String key = new String(reader.readLine());
//                      System.out.println("you typed " + key);
//                      if ("y".equals(key) ) { 
//                            System.out.println("you typed yes and the file has been dleted"  );
//                            deleteFile(service,file.getId());
//                          
//                      }
//
//                    } catch (IOException e) 
//                    {
//                      e.printStackTrace();
//                    } 
              
  } // end googleTest
    
        
    
   private void switchToDriveShowUp()    
   { 
       try
       {
          mainStage = Drivecopycheck.getStagefromMain();
          FXMLLoader loader = new FXMLLoader(getClass().getResource("DriveFXML.fxml"));
          Parent mainRoot = new AnchorPane((AnchorPane) loader.load());
          FXMLDriveController controller = (FXMLDriveController) loader.getController();
          controller.populateRoot(currentCloudDriveObject, Drivecopycheck.service);
          Scene scene = new Scene(mainRoot);
          mainStage.setScene(scene);
          mainStage.show();
       }
        catch (Exception ex) 
        {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex); 
        }
    }
    
    
   private void deleteCookies(){
        System.out.println("deleteCookies+++++++++++ executing..." );
        cookieMan.getCookieStore().removeAll();
    }

    
   private void writeCredentialsToFile()
   {
        Properties prop = new Properties();
        OutputStream output = null;
        try { 
                output = new FileOutputStream("config.properties");
                client_id = client_id_txtField.getText().trim();
                client_secret = client_secret_PasswoedField.getText().trim();
                // set the properties value
                prop.setProperty("client_id",  client_id.trim());
                prop.setProperty("client_secret", client_secret.trim());
             
                // save properties to project root folder
                prop.store(output, null);

	} catch (IOException io) {
		io.printStackTrace();
	} 
                finally 
                {
	   if (output != null) 
                    {
                                 try {
                                         output.close();
                                         System.out.println("file closed succesfully");
                                         save_btn.setText("Saved");
                                         save_btn.setDisable(true);
                                         urlToGo = "https://accounts.google.com/o/oauth2/auth?access_type=" + access_type + 
                   "&approval_prompt=auto&client_id=" + client_id + 
                   "&redirect_uri=urn:ietf:wg:oauth:2.0:oob&response_type=code&" + 
                   "scope=https://www.googleapis.com/auth/drive https://www.googleapis.com/auth/drive.appdata https://www.googleapis.com/auth/drive.apps.readonly https://www.googleapis.com/auth/drive.file https://www.googleapis.com/auth/drive.metadata https://www.googleapis.com/auth/drive.metadata.readonly https://www.googleapis.com/auth/drive.readonly";
                                webEngine.load(urlToGo);  
                                 } 
                                 catch (IOException e) {
                                            e.printStackTrace();
                                 }
                     }
                }
   }
   
    private void readCredentialsFromFile() throws IOException, ParserConfigurationException, SAXException {
               
         Properties prop = new Properties();
         InputStream input = null;
         
         try
         { 
                    input = new FileInputStream(fileNameCredentials);

                   // load a properties file
                    prop.load(input);
 
                    client_id = prop.getProperty("client_id");
                    client_secret =prop.getProperty("client_secret");

                    client_id_txtField.setText(client_id);
                    client_secret_PasswoedField.setText(client_secret);

                    save_btn.setText("Saved");
                    save_btn.setDisable(true); 
                    System.out.println("++++++++++client_id=" +client_id);
                    System.out.println("++++++client_secret=" +client_secret);
                    System.out.println("--------------------------");
         }
         catch (IOException ex) {
                    ex.printStackTrace();
         }
         finally
             {
                 if (input != null) 
                 {
                        try
                            {
                                input.close();
                            } 
                            catch (IOException e) 
                            {
                                e.printStackTrace();
                            }
                 }
                  
             }
         
       
        
    }
     
     
    
    
    
     
//   private static void deleteFile(Drive service, String fileId) {
//    try {
//        service.files().delete(fileId).execute();
//        } catch (IOException e) {
//            System.out.println("An error occurred: " + e);
//        }
//    }
    
   
   
   public class HTMLCommunication {
 
        public void attachOnClickHandler() throws Exception {
            Drivecopycheck.emailAddressLogin = webEngine.getDocument().getElementById("email-display").getTextContent();
            System.out.println("------------------- Last typed email------------=" +   Drivecopycheck.emailAddressLogin );
            
            if (  emailAddressIsManaged( Drivecopycheck.emailAddressLogin))   
            { 
                webView.setVisible(false); // shuts down the webview visibility 
                managedEmailAddress = true;
               // System.out.println("managedEmailAddressVariable=" +  managedEmailAddressVariable );
            }
            else 
            { 
                webView.setVisible(true);
                managedEmailAddress = false;
            } // shuts down the webview visibility 
        }
    } // end of HTMLCommunication class
      
} // main class

 
