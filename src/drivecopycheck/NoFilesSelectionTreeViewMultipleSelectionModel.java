/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drivecopycheck;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 *
 * @author marx
 */
  
   public  class NoFilesSelectionTreeViewMultipleSelectionModel extends MultipleSelectionModel<TreeItem<Object>> {

        private final MultipleSelectionModel<TreeItem<Object>> selectionModel ;
        private final TreeView<Object> treeView ;

        public NoFilesSelectionTreeViewMultipleSelectionModel(MultipleSelectionModel<TreeItem<Object>> selectionModel, TreeView<Object> treeView) {
            this.selectionModel = selectionModel ;
            this.treeView = treeView ;
            selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
            selectionModeProperty().bindBidirectional(selectionModel.selectionModeProperty());
        }

        @Override
        public ObservableList<Integer> getSelectedIndices() {
           // System.out.println("selectionModel.getSelectedIndices()" + selectionModel.getSelectedIndices());
            return selectionModel.getSelectedIndices() ;
        }

        @Override
        public ObservableList<TreeItem<Object>> getSelectedItems() {
            //System.out.println("selectionModel.getSelectedIndices()" +selectionModel.getSelectedItems());
            return selectionModel.getSelectedItems() ;
        }

        @Override
        public void selectIndices(int index, int... indices) { 
           System.out.println("po");
          
            List<Integer> indicesToSelect = Stream.concat(Stream.of(index), IntStream.of(indices).boxed())
                    .filter(i -> ( treeView.getTreeItem(i).getChildren().size() > 0 ) && (i != 0) )
                    .collect(Collectors.toList());


            if (indicesToSelect.isEmpty()) { 
                return ;
            }
            selectionModel.selectIndices(indicesToSelect.get(0), 
                    indicesToSelect.stream().skip(1).mapToInt(Integer::intValue).toArray());
           
        }

        @Override
        public void selectAll() {
//            List<Integer> indicesToSelect = IntStream.range(0, treeView.getExpandedItemCount())
//                    .filter(i -> treeView.getTreeItem(i).getChildren().size() > 0)
//                    .boxed()
//                    .collect(Collectors.toList());
//            if (indicesToSelect.isEmpty()) {
//                return ;
//            }
//            selectionModel.selectIndices(0, 
//                    indicesToSelect.stream().skip(1).mapToInt(Integer::intValue).toArray());
       
        }

        @Override
        public void selectFirst() {
//            IntStream.range(0, treeView.getExpandedItemCount())
//                .filter(i -> treeView.getTreeItem(i).getChildren().size() > 0)
//                .findFirst()
//                .ifPresent(selectionModel::select);
        }

        @Override
        public void selectLast() {
//            IntStream.iterate(treeView.getExpandedItemCount() - 1, i -> i - 1)
//                .limit(treeView.getExpandedItemCount())
//                .filter(i -> treeView.getTreeItem(i).getChildren().size() > 0)
//                .findFirst()
//                .ifPresent(selectionModel::select);
           
        }

        @Override
        public void clearAndSelect(int index) {
//            int toSelect = index ;
//            int direction = selectionModel.getSelectedIndex() < index ? 1 : -1 ;
//            while (toSelect >= 0 && toSelect < tree.getExpandedItemCount() && ! (tree.getTreeItem(toSelect).getValue() instanceof Tour)) {
//                toSelect = toSelect + direction  ;
//            }
//            if (toSelect >= 0 && toSelect < tree.getExpandedItemCount()) {
//                selectionModel.clearAndSelect(toSelect);
//            }
            System.out.println("public void clearAndSelect(int index)");
            if ( index != 0)
            {
                boolean isFolderwithContent = this.treeView.getTreeItem(index).getChildren().size() > 0;
                if ( isFolderwithContent )  selectionModel.clearAndSelect(index);
            }
        }

        @Override
        public void select(int index) {
           
          System.out.println("select(  " + index + "  )");
//            int toSelect = index ;
//            int direction = selectionModel.getSelectedIndex() < index ? 1 : -1 ;
//           //System.out.println("selectionModel.getSelectedIndex()=" + selectionModel.getSelectedIndex());
//            while (toSelect >= 0 && toSelect < tree.getExpandedItemCount() && ! (tree.getTreeItem(toSelect).getValue() instanceof Tour)) {
//                toSelect = toSelect + direction  ;
//            }
//            if (toSelect >= 0 && toSelect < tree.getExpandedItemCount()) {
//                selectionModel.select(toSelect);
//            }
           // System.out.println("public void select(int index)" +  this.treeView.getTreeItem(index));  
            //if (  treeItemWithLinkObject.isFolder() )  selectionModel.select(index);
            if ( index != 0)
            {
                boolean isFolderwithContent = this.treeView.getTreeItem(index).getChildren().size() > 0;
                if ( isFolderwithContent ) 
                {
                    clearSelectionOfParents(index);
                    clearSelectionOfChildren(index);
                    selectionModel.select(index);
                }
            }
        }
        
        private void clearSelectionOfChildren(int index)
        {
            System.out.println("Scanning index passed ="+ index );
            TreeItem itemToScan =  this.treeView.getTreeItem(index) ;
               ObservableList itemsToScan ;
               itemsToScan = itemToScan.getChildren();
               for ( Object childToScan: itemsToScan)
               {
                   TreeItem convertedObjectToScan = ( TreeItem ) childToScan;
                   if ( this.getSelectedItems().contains(convertedObjectToScan) ) 
                   {
                       convertedObjectToScan.setExpanded(true);
                       this.clearSelection( this.treeView.getRow(convertedObjectToScan)); 
                   }              
           
               }
        }
        
        private void clearSelectionOfParents(int index)
        {
          if ( index ==0 ) return;
          TreeItem itemToScan =  this.treeView.getTreeItem(index) ;
           do
           {
               System.out.println("Scanning index passed ="+ index );
               itemToScan = itemToScan.getParent();
               System.out.println("itemToScan=" + itemToScan);
               //System.out.println("Selection to clear at index=" + this.getSelectedItems().indexOf(itemToScan));
               if ( this.getSelectedItems().contains(itemToScan) ) 
               { 
                   System.out.println("Selection to clear at index=" + this.getSelectedItems().indexOf(itemToScan));
                   this.clearSelection( this.treeView.getRow(itemToScan)); 
                    // itemToScan = itemToScan.getParent();
               }
              
            } while ( itemToScan.getValue() != "Drive" );
        }

        @Override
        public void select(TreeItem<Object> obj) {
           //System.out.println("obj.getValue()="+ obj.getValue().toString());
//            if (obj.getValue() instanceof Tour) {
//                selectionModel.select(obj);
//            }
            System.out.println("public void select(TreeItem<Object> obj)");
            selectionModel.select(obj);
        }

        @Override
        public void clearSelection(int index) {
            System.out.println("clearSelection(int index)");
            selectionModel.clearSelection(index);
        }

        @Override
        public void clearSelection() {
            System.out.println("clearSelection()");
            selectionModel.clearSelection();
        }

        @Override
        public boolean isSelected(int index) {
            //System.out.println("isSelected" + index);
            return selectionModel.isSelected(index);
        }

        @Override
        public boolean isEmpty() {
            return selectionModel.isEmpty();
        }

        @Override
        public void selectPrevious() {
//            System.out.println("selectPrevious");
//            int current = selectionModel.getSelectedIndex() ;
//            if (current > 0) {
//                IntStream.iterate(current - 1, i -> i - 1).limit(current)
//                    .filter(i -> tree.getTreeItem(i).getValue() instanceof Tour)
//                    .findFirst()
//                    .ifPresent(selectionModel::select);
//            }
            //selectionModel.selectPrevious();
        }

        @Override
        public void selectNext() {
//            int current = selectionModel.getSelectedIndex() ;
//            if (current < tree.getExpandedItemCount() - 1) {
//                IntStream.range(current + 1, tree.getExpandedItemCount())
//                    .filter(i -> tree.getTreeItem(i).getValue() instanceof Tour)
//                    .findFirst()
//                    .ifPresent(selectionModel::select);
//            }
        //selectionModel.selectNext();
        }

    }