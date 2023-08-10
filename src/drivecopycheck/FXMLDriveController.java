/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drivecopycheck;

import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.net.URL;


import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;



import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;


 
public class FXMLDriveController implements Initializable {
    
    private TreeItemWithLink treeItem;
 
    @FXML
    private TextField httpTextfitreeItemeld;
     
    @FXML
    public TreeView treeviewID; 

    @FXML
    private DatePicker mTimeFrom;
    
    @FXML
    private DatePicker cTimeFrom;
    
    @FXML
    private HBox hBoxLinkTree;
     
   @FXML
    private DatePicker mTimeTo;
    
    @FXML
    private DatePicker cTimeTo;

    @FXML
    private Button searchButton;

    @FXML
    private Button button3;
 
    @FXML
    private Button button31;

    @FXML
    private TextField startTextField;

    @FXML
    private Label foldersTotal;
    
    @FXML
    private HBox hBoxCreated;
    
    @FXML
    private HBox hBoxModified;
 
    @FXML
    private Label foldersSelectedTotal;
 
    @FXML
    private Label itemsTotal;

    @FXML
    private ListView listView;

    @FXML
    private HBox hBoxSearchStats;
    
    @FXML
    private HBox hBoxLink;

    @FXML
    private Label filesTotal;

    @FXML
    private TextField endsTextField;

    @FXML
    private Label foundItems;

    @FXML
    private Button button1;

    @FXML
    private Label trashedTotal;

    @FXML
    private TextField containsTextfield;
    
    @FXML
    private Label creationDate;
    
    @FXML
    private Label modifDate;
    
    @FXML
    private Label creationDateTree;
    
    @FXML
    private Label modifDateTree;
    
    @FXML
    private TextField httpTextfieldTree;
    
    @FXML
    private TextField httpTextfield;
    
    @FXML
    private HBox hBoxCreatedTree;
    
     @FXML
    private HBox hBoxModifiedTree;
     
    @FXML
    private CheckBox onlytSelectedFolderSearch;
    
   
    private Node googleDriveIcon  = new ImageView( new Image(getClass().getResourceAsStream("google-drive-icon.png")));
    
    private ObservableList<File> tempListForSearchBox ;
    
    private  ObservableList<String> tempStringList;
    private int itemsTotalVar=0;
    private int foldersTotalVar=0;
    private int filesTotalVar=0;
    private int trashedTotalVar=0;
    public Drive service;
    private ObservableList  selectionListInTree;
    
 
    
    @FXML
    private void goAndSearchHandler(ActionEvent event) throws IOException 
    { 
        this.tempListForSearchBox = goAndSearch();
        this.tempStringList = FXCollections.observableList(new ArrayList());
        
        if ( ! (tempListForSearchBox.isEmpty() ))  
        {
            hBoxSearchStats.setDisable(false);
            foundItems.setText((Integer.toString(tempListForSearchBox.size())));
            for ( File aFile : tempListForSearchBox )
            {
                tempStringList.add(aFile.getTitle());
            }
        }
         listView.setItems(this.tempStringList);
      
    }
    
    @FXML
       private void searchedClickHandler(MouseEvent event) {
         if ( listView.getItems() != null)
            if ( listView.getItems().size() > 0 )
                {
                   int selectedIndex = listView.getSelectionModel().getSelectedIndex() ;
                   hBoxLink.setDisable(false);
                   hBoxCreated.setDisable(false);
                   hBoxModified.setDisable(false);
                   httpTextfield.setText(tempListForSearchBox.get(selectedIndex).getAlternateLink());
                   creationDate.setText(tempListForSearchBox.get(selectedIndex).getCreatedDate().toStringRfc3339().substring(0, 10));
                   modifDate.setText(tempListForSearchBox.get(selectedIndex).getModifiedDate().toStringRfc3339().substring(0, 10));
//                  
//                   System.out.println(">>>>>>>>Date=" +  tempListForSearchBox.get(selectedIndex).getModifiedDate().toStringRfc3339().substring(0, 10));
//                   System.out.println(">>>>>>>>selectedIndex=" + selectedIndex);
//                   System.out.println(">>>>>>>>tempListForSearchBox.size()=" + tempListForSearchBox.size());
//
//                   System.out.println("double click on Search results >>>" + selectedIndex  + "||| "  + this.tempListForSearchBox.get(selectedIndex).toString() ) ;  
//                   System.out.println(  this.tempListForSearchBox.get(selectedIndex).getAlternateLink()) ;  
//                   System.out.println(">>>>>>>>>>>>>>>>>>>>");
                   }
    }
       
//    @FXML
//    private void handleTreeClick(MouseEvent event) {
//        
//        TreeItemWithLink clickedItem = ( TreeItemWithLink )   treeviewID.getSelectionModel().getSelectedItem();
//      //  System.out.println("clickedItem>>>>>" + clickedItem  );
//        System.out.println("selectionListInTree.size=" + selectionListInTree.size());
//        //System.out.println("double click on treeMenu >>>>>" +  treeviewID.getSelectionModel().getSelectedIndex()); 
//        //System.out.println("double click on treeMenu  treeItem.getValue() >>>>>" +  treeviewID.getTreeItem( treeviewID.getSelectionModel().getSelectedIndex())); 
//        hBoxLinkTree.setDisable(false);
//        hBoxCreatedTree.setDisable(false);
//        hBoxModifiedTree.setDisable(false);
//        if (clickedItem != null)
//        {
//                httpTextfieldTree.setText( clickedItem.getID());
//                creationDateTree.setText(clickedItem.getCreationDate().toString().substring(0, 10));
//                modifDateTree.setText(clickedItem.getModifiedDate().toString().substring(0, 10)); 
//        }
//        if ( selectionListInTree.size() > 1)
//        {
//                httpTextfieldTree.setText( "");
//                creationDateTree.setText("");
//                modifDateTree.setText(""); 
//        }
//         foldersSelectedTotal.setText(GetSelectedFolders() );
//    }
    
    private ListChangeListener listenerForChangesinSelection =  ( ListChangeListener ) (ListChangeListener.Change  c)-> {
       // System.out.println("c=" + c);
         while(c.next()) {
                        if (c.wasAdded())
                        {
                            hBoxLinkTree.setDisable(false);
                            hBoxCreatedTree.setDisable(false);
                            hBoxModifiedTree.setDisable(false);
                            //System.out.println("Added");
                            TreeItemWithLink selectedItem = ( TreeItemWithLink ) c.getAddedSubList().get(0);
                            if ( ( c.getAddedSubList().size()  < 2 )  && ! (selectedItem.getID().equals("rootID")   ))
                            {
                                httpTextfieldTree.setText( selectedItem.getID());
                                creationDateTree.setText(selectedItem.getCreationDate().toString().substring(0, 10));
                                modifDateTree.setText(selectedItem.getModifiedDate().toString().substring(0, 10));
                            }
                        }
                        if ( c.wasRemoved())
                        {
                            hBoxLinkTree.setDisable(true);
                            hBoxCreatedTree.setDisable(true);
                            hBoxModifiedTree.setDisable(true);
                            httpTextfieldTree.setText( "");
                            creationDateTree.setText("");
                            modifDateTree.setText("");
                            TreeItemWithLink selectedItem = ( TreeItemWithLink ) c.getRemoved().get(0);
                            if ( ( c.getRemoved().size()  < 2 )  && ! (selectedItem.getID().equals("rootID")   ))
                            {
                                httpTextfieldTree.setText( selectedItem.getID());
                                creationDateTree.setText(selectedItem.getCreationDate().toString().substring(0, 10));
                                modifDateTree.setText(selectedItem.getModifiedDate().toString().substring(0, 10));
                            }
                        }
                        foldersSelectedTotal.setText(GetSelectedFolders());
                        
                    }
        
            };
    
    
    
    
     @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        System.out.println("initialize called from FXMLDriveController.java----------------------------------------");
        listView.setItems(tempStringList);
        cTimeTo.setValue(LocalDate.now());
        cTimeTo.setDayCellFactory(dayCellFactoryForTo);
        // System.out.println("cTimeFrom.getDayCellFactory()???" + cTimeFrom.getDayCellFactory()); 
        cTimeFrom.setDayCellFactory(dayCellFactoryForFrom); 
        // System.out.println("cTimeFrom.getDayCellFactory()???" + cTimeFrom.getDayCellFactory()); 
         treeviewID.setSelectionModel(new NoFilesSelectionTreeViewMultipleSelectionModel(treeviewID.getSelectionModel(), treeviewID));
         treeviewID.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); 
        
         this.selectionListInTree=  treeviewID.getSelectionModel().getSelectedItems();
         selectionListInTree.addListener(listenerForChangesinSelection);
         
    }
    
    
      private void parentClearing(TreeItemWithLink clickedItem) {
        TreeItemWithLink currentitem = clickedItem;
       do
       {
           if (  selectionListInTree.contains(currentitem.getParent()))   
           {
               System.out.println("Fired and removed::::::::::::::::currentItem.getParent()=" + currentitem.getParent() + " with Index= " + treeviewID.getRow(currentitem.getParent()  )); 
               treeviewID.getSelectionModel().clearSelection( treeviewID.getRow(currentitem.getParent()) ); 
           }
           currentitem = (TreeItemWithLink) currentitem.getParent();
  
       }while (currentitem.getParent() != null);
    }
 

   private boolean selectionListHasParent(File aFile)
   {
       if (aFile!= null)
       {
       if ( ! onlytSelectedFolderSearch.isSelected()) return true;
       // exit above
       for ( Object  eachObject :   this.selectionListInTree) 
       {
           System.out.println("----------eachObject=" + eachObject);
           TreeItemWithLink itemOfTree = ( TreeItemWithLink ) eachObject;
           if (itemOfTree.getID().split("id=").length > 1)
                {
                        System.out.println("itemOfTree.getID()                   =" + itemOfTree.getID().split("id=")[1].split("&usp=")[0]);
                        if (aFile.getParents() != null)
                            if (aFile.getParents().size() > 0) 
                            { 
                                 System.out.println("VALID");
                                 System.out.println("aFile.getParents().get(0).getId() =" + aFile.getParents().get(0).getId());
                                 String itemOfTreeID = itemOfTree.getID().split("id=")[1].split("&usp=")[0];
                                 String aFileParentID = aFile.getParents().get(0).getId();
                                 if ( aFileParentID.equals(itemOfTreeID)) return true;
                            }
                        else System.out.println("NOT VALID");
                }
       } 
       }
       return false;
   }
     
    
  private String GetSelectedFolders()
    {
        Integer count = 0;
        for ( Object anObject : this.selectionListInTree )
        {
            TreeItemWithLink eachItemOfTree = ( TreeItemWithLink ) anObject;
            if (eachItemOfTree != null)
            if (  (eachItemOfTree.isFolder())) count++;
        }
        return count.toString();
    }
    
    
    
    public Callback<DatePicker, DateCell> dayCellFactoryForTo = 
            new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                            if ( cTimeFrom.getValue() != null )
                            {
                                if (item.isBefore(cTimeFrom.getValue()))                                 
                                    {
                                        setDisable(true);
                                        setStyle("-fx-background-color: #ffc0cb;");
                                    }
                            }
                            if ( item.isAfter(LocalDate.now()))
                               {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                    }
                };
            }
        };
    
    
    final Callback<DatePicker, DateCell> dayCellFactoryForFrom = 
            new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                            if ( cTimeTo.getValue() != null )
                            {
                                if (item.isAfter(cTimeTo.getValue() )) 
                                    {
                                        setDisable(true);
                                        setStyle("-fx-background-color: #ffc0cb;");
                                    }
                            }
                            if ( item.isAfter(LocalDate.now()))
                               {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                    }
                };
            }
        };
    
 
    @FXML
    void dateCheckHandler(ActionEvent event) 
    {
         System.out.println("dateCheckHandlerVVVVVVVV");
//         System.out.println(">>>>>>>>>>>>>>>>>>>> cTimeFrom== " + cTimeFrom.getValue());
//         System.out.println(">>>>>>>>>>>>>>>>>>>> cTimeTo== " + cTimeTo.getValue());
         DatePicker whatHasbeenClicked = ( DatePicker ) event.getSource();
         if ( whatHasbeenClicked == cTimeTo )
         {   
                if ( (cTimeFrom.getValue() == null ))  cTimeFrom.setDayCellFactory(dayCellFactoryForFrom); 
                else  cTimeFrom.setDayCellFactory(dayCellFactoryForFrom); 
         }
         if ( whatHasbeenClicked == cTimeFrom)  
         { 
             cTimeTo.setDayCellFactory(dayCellFactoryForTo);
         } 
    }
    
     
    
    public ObservableList<File> goAndSearch() throws IOException
     {
            List<File> tempList = new ArrayList<File>();
            for ( File aFile : retrieveSearchedFiles(Drivecopycheck.service ))
            { 
                tempList.add(aFile);
            }
            return FXCollections.observableList(new ArrayList(new HashSet(tempList)));
     }
     
    
     // cTimeFrom
    private  List<File> retrieveSearchedFiles(Drive service) throws IOException 
     {
        List<File> result = new ArrayList<File>();
        Date cTimeFromdate = null;
        Date cTimeTodate = null;
        Boolean wordsSearchState;
        System.out.println("cTimeFrom:::::::::" +   cTimeFrom.getValue());
        if ( cTimeFrom.getValue() != null )  cTimeFromdate= java.sql.Date.valueOf( cTimeFrom.getValue());
        if ( cTimeTo.getValue() != null )       
        { 
            cTimeTodate= java.sql.Date.valueOf( cTimeTo.getValue());
            cTimeTodate.setTime(cTimeTodate.getTime() + 86400000 - 1);
        }
        
        for ( File aFile : Drivecopycheck.globalRepoList ) 
        { 
            wordsSearchState = (aFile.getTitle().contains( containsTextfield.getText()) && 
                  aFile.getTitle().startsWith(startTextField.getText())    &&
                  aFile.getTitle().endsWith(endsTextField.getText()) )&& 
                  selectionListHasParent(aFile);
             //System.out.println("aFile@@@@" +  aFile.getTitle() + "   " + aFile. getOwners());
            if ( wordsSearchState )
            {
                    System.out.println(" cTimeFrom.getValue()  >> " + cTimeFrom.getValue());
                    System.out.println("Element >> " + aFile.getTitle());
                    System.out.println("aFile.getCreatedDate()  >> " + new Date(aFile.getCreatedDate().getValue()) );
                  if ( cTimeFromdate != null ) 
                  {
                     if (cTimeFromdate.before(new Date(aFile.getCreatedDate().getValue()))  )  // aqui
                     {
                          if ( cTimeTodate != null ) 
                          { 
                               if (cTimeTodate.after(new Date(aFile.getCreatedDate().getValue())))  result.add(aFile);
                              // System.out.println("cTimeTodate != null" + cTimeTodate.toString());
                             
                          }
                          else   result.add(aFile);
                     } 
                  }
                  else
                  {
                        // second stage of the checks
                        if ( cTimeTodate != null ) 
                        {
                           if  (cTimeTodate.after(new Date(aFile.getCreatedDate().getValue() )))
                           {
                                result.add(aFile);
                           } 
                        }
                        else  result.add(aFile);
                  }
            }
             //System.out.println("aFile.getParents=" +  	aFile.getParents().toString());
        }  
         System.out.println("#of elements from the search:::::::::" + result.size());
        return result;
  }
    
    
    
    
    public void populateRoot(List<File> cloudDriveObject, Drive servicePassed) throws IOException 
    {
        List<File> tempParentList= new ArrayList<File>();  
        treeItem = new drivecopycheck.TreeItemWithLink("Drive","rootID", googleDriveIcon, true);
        treeviewID.setRoot(treeItem); 
   
        treeItem.setExpanded(true); 
        for (File cloudDriveObjectCurrent : cloudDriveObject) 
        {
            if (!(cloudDriveObjectCurrent.getParents().isEmpty())) 
            {
               if (cloudDriveObjectCurrent.getParents().get(0).getIsRoot()) 
               {
                   if (!(cloudDriveObjectCurrent.getLabels().getTrashed())) 
                   {
                       if ((cloudDriveObjectCurrent.getMimeType().equals("application/vnd.google-apps.folder"))) 
                       { // folder item
                              tempParentList.add(cloudDriveObjectCurrent); 
                              Node rootIcon = new ImageView( new Image(getClass().getResourceAsStream("folder_16.png"))); 
                              TreeItemWithLink currentTreeFolderItem= new TreeItemWithLink(cloudDriveObjectCurrent.getTitle() , cloudDriveObjectCurrent.getAlternateLink() , rootIcon, true );
                              currentTreeFolderItem.setCreationDate(   cloudDriveObjectCurrent.getCreatedDate());
                              currentTreeFolderItem.setModifiedDate(   cloudDriveObjectCurrent.getModifiedDate());
                              treeItem.getChildren().addAll(currentTreeFolderItem);
                              foldersTotalVar++;
                              populateFolder(cloudDriveObject , cloudDriveObjectCurrent, currentTreeFolderItem);
                       }
                       else // child item
                       { 
                              Node fileIcon   = new ImageView( new Image(getClass().getResourceAsStream("file_16.png")));
                              TreeItemWithLink currentLeafItem = new TreeItemWithLink(cloudDriveObjectCurrent.getTitle(), cloudDriveObjectCurrent.getAlternateLink(), fileIcon , false);
                              currentLeafItem.setCreationDate(  cloudDriveObjectCurrent.getCreatedDate());
                              currentLeafItem.setModifiedDate(  cloudDriveObjectCurrent.getModifiedDate());
                              treeItem.getChildren().addAll(currentLeafItem);
                              filesTotalVar++;
                       }
                   }
                    else trashedTotalVar++;
               }
            }
         } 
        UpdateStatsLabels();
    }
    
    
    public void populateFolder(List<File> cloudDriveObject, File cloudDriveObjectCurrent, TreeItemWithLink currentTreeFolderItem) throws IOException 
    {
        for (File cloudDriveObjectCurrent1 : getFilesInFolder(cloudDriveObjectCurrent.getId() ,cloudDriveObject )) 
        {
             if (!(cloudDriveObjectCurrent1.getParents().isEmpty())) 
             {
                 if (!(cloudDriveObjectCurrent1.getLabels().getTrashed())) 
                 {
                     if ((cloudDriveObjectCurrent1.getMimeType().equals("application/vnd.google-apps.folder"))) 
                     { 
                            Node rootIcon = new ImageView( new Image(getClass().getResourceAsStream("folder_16.png"))); 
                            TreeItemWithLink currentTreeFolderItem1= new TreeItemWithLink(cloudDriveObjectCurrent1.getTitle(), cloudDriveObjectCurrent1.getAlternateLink() , rootIcon, true);
                             currentTreeFolderItem1.setCreationDate(  cloudDriveObjectCurrent1.getCreatedDate());
                             currentTreeFolderItem1.setModifiedDate(  cloudDriveObjectCurrent1.getModifiedDate());                            
                            currentTreeFolderItem.getChildren().addAll(currentTreeFolderItem1);
                            foldersTotalVar++;
                            populateFolder(cloudDriveObject, cloudDriveObjectCurrent1 ,currentTreeFolderItem1);
                     }
                     else 
                     { 
                            Node fileIcon   = new ImageView( new Image(getClass().getResourceAsStream("file_16.png")));
                            TreeItemWithLink currentLeafItem =new TreeItemWithLink(cloudDriveObjectCurrent1.getTitle(), cloudDriveObjectCurrent1.getAlternateLink(), fileIcon, false);
                            currentLeafItem.setCreationDate(  cloudDriveObjectCurrent1.getCreatedDate());
                            currentLeafItem.setModifiedDate(  cloudDriveObjectCurrent1.getModifiedDate());
                            currentTreeFolderItem.getChildren().addAll(currentLeafItem);
                            filesTotalVar++;
                     }
                 }
                 else trashedTotalVar++;
             }
         } 
     }
     
    
    private static List<File> getFilesInFolder(String folderId , List<File> cloudDriveObject) throws IOException 
    {
            List<File> tempList = new ArrayList<File>();  
              for (File cloudDriveObjectCurrent : cloudDriveObject) {
                if (!(cloudDriveObjectCurrent.getParents().isEmpty())) {
                    if (!(cloudDriveObjectCurrent.getLabels().getTrashed())) {
                        if (cloudDriveObjectCurrent.getParents().get(0).getId().equals(folderId)) {
                               tempList.add(cloudDriveObjectCurrent); 
                        }
                    }
                } 
              } 
            return tempList;
    }

    
    private void UpdateStatsLabels() 
    {
        itemsTotalVar = foldersTotalVar + filesTotalVar;
        itemsTotal.setText(Integer.toString(itemsTotalVar));
        foldersTotal.setText(Integer.toString(foldersTotalVar));
        filesTotal.setText(Integer.toString(filesTotalVar));
        trashedTotal.setText(Integer.toString(trashedTotalVar));
    }

    
    
    
  


 
     
    
} // end FXMLDocumentMainController
                          
                                   
       
   
    

