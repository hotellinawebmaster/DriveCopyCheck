/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drivecopycheck;

import com.google.api.client.util.DateTime;
import java.util.Date;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;

/**
 *
 * @author marx
 */
public class TreeItemWithLink extends TreeItem<String> {
    private String alternateLink;
    private DateTime creationDate;
    private DateTime modifiedDate;    
    private boolean folderType;
    
    public TreeItemWithLink(boolean afolderType){
        super();
        this.setID("emptyID");
        this.folderType = afolderType;
    }
    
    public TreeItemWithLink(String aString, String anID,  Node graphic , boolean afolderType){
        super(aString, graphic);
        this.setID(anID);
        this.folderType = afolderType;
    }
    
    public void setID(String anID){
        this.alternateLink = anID;
    }
    
    public String getID(){
        return this.alternateLink;
    }
     
    public void setCreationDate(DateTime aDate)
    {
        this.creationDate = aDate;
    }
    
    public void setModifiedDate(DateTime aDate)
    {
        this.modifiedDate = aDate;
    }    
    public DateTime getCreationDate()
    {
        return this.modifiedDate;
    }
    
    public DateTime getModifiedDate()
    {
        return this.modifiedDate;
    }      
    
    public boolean isFolder(){
        return this.folderType;
    }
}
