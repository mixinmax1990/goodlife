package com.news.goodlife.Functions;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.news.goodlife.CustomViews.RelationshipMapView;
import com.news.goodlife.Models.ModuleCoords;
import com.news.goodlife.Models.RelationshipMap;
import com.news.goodlife.R;

import java.util.ArrayList;
import java.util.List;

public class RelationshipMapping {

    List<RecyclerView.ViewHolder> visibleDays;

    public RelationshipMapping() {
    }

    public List<RecyclerView.ViewHolder> getVisibleDays() {
        return visibleDays;
    }

    public void setVisibleDays(List<RecyclerView.ViewHolder> visibleDays) {
        this.visibleDays = visibleDays;
    }




    public void mapRelationships(){

        //Log.i("Relationship Map for", ""+getVisibleDays().size()+" items");

        for(RecyclerView.ViewHolder viewHolder: visibleDays){
            // TODO LOAD DATA ASYNCHRONOUSLY if necessary

            List<ModuleCoords> moduleCoords = new ArrayList<>();
            List<RelationshipMap> relationshipMapData = new ArrayList<>();

            int moduleCenterX, moduleCenterY;

            View spend_module, income_module, balance_module, savings_container;
            ViewGroup budget_container;
            RelationshipMapView mapView;


            //Get all the Views
            spend_module = viewHolder.itemView.findViewById(R.id.fixed_costs_module);
            income_module = viewHolder.itemView.findViewById(R.id.events_incoming);
            balance_module = viewHolder.itemView.findViewById(R.id.cashcat_frame);
            budget_container = viewHolder.itemView.findViewById(R.id.month_flex);
            savings_container = viewHolder.itemView.findViewById(R.id.savings_module);

            //Get the MapView

            mapView = viewHolder.itemView.findViewById(R.id.overlay_relation_map);

            moduleCenterX = (int)spend_module.getX() + (spend_module.getWidth() / 2);
            moduleCenterY = (int)spend_module.getY() + (spend_module.getHeight() / 2);
            ModuleCoords mc = new ModuleCoords(moduleCenterX, moduleCenterY, "spend");
            moduleCoords.add(mc);

            moduleCenterX = (int)income_module.getX() + (income_module.getWidth() / 2);
            moduleCenterY = (int)income_module.getY() + (income_module.getHeight() / 2);
            mc = new ModuleCoords(moduleCenterX, moduleCenterY, "income");
            moduleCoords.add(mc);

            moduleCenterX = (int)balance_module.getX() + (balance_module.getWidth() / 2);
            moduleCenterY = (int)balance_module.getY() + (balance_module.getHeight() / 2);
            mc = new ModuleCoords(moduleCenterX, moduleCenterY, "balance");
            moduleCoords.add(mc);

            int savingsCenterX = moduleCenterX;
            int savingsCenterY = moduleCenterY;

            moduleCenterX = (int)savings_container.getX() + (savings_container.getWidth() / 2);
            moduleCenterY = (int)savings_container.getY() + (savings_container.getHeight() / 2);
            mc = new ModuleCoords(moduleCenterX, moduleCenterY, "savings");
            moduleCoords.add(mc);

            relationshipMapData.add(connectRelationships(moduleCoords, "balance", "savings"));
            relationshipMapData.add(connectRelationships(moduleCoords, "savings", "goals"));


            //Iterate thru all the Budgets -> Set Coords and Draw Lines afterwards
            mapView.setMapData(iterateBudgets(budget_container, relationshipMapData, savingsCenterX, savingsCenterY));

        }

    }

    private List<RelationshipMap> iterateBudgets(ViewGroup budgetCont, List<RelationshipMap> relationshipMapData, int savingsCenterX, int savingsCenterY){

        int childCount = budgetCont.getChildCount();

        Log.i("ChildCount", ""+childCount);
       /* for(int i = 0; i < childCount; i++){

            int moduleCenterX, moduleCenterY;

            View budgetIcon = budgetCont.getChildAt(i).findViewById(R.id.budget_icon_cont);

            moduleCenterX = (int)budgetIcon.getX() + (budgetIcon.getWidth() / 2);
            moduleCenterY = (int)budgetCont.getY() + (int)budgetIcon.getY() + (budgetIcon.getHeight() / 2);

            Log.i("Child"+i, " X = "+moduleCenterX +"; Y = "+moduleCenterY);

            RelationshipMap relationshipConnection = new RelationshipMap();

            relationshipConnection.setFromX(savingsCenterX);
            relationshipConnection.setFromY(savingsCenterY);

            relationshipConnection.setToX(moduleCenterX);
            relationshipConnection.setToY(moduleCenterY);

            relationshipMapData.add(relationshipConnection);

        } */

        return relationshipMapData;

    }

    private RelationshipMap connectRelationships(List<ModuleCoords> moduleCoords, String fromID, String toID){
        RelationshipMap relationshipConnection = new RelationshipMap();

        for(ModuleCoords moduleCoord: moduleCoords){
            if(moduleCoord.getName().equals(fromID)){
                //From Match Found set relationshipConnection
                relationshipConnection.setFromX(moduleCoord.getX());
                relationshipConnection.setFromY(moduleCoord.getY());
            }

            if(moduleCoord.getName().equals(toID)){
                //To Match Found setRelationships
                relationshipConnection.setToX(moduleCoord.getX());
                relationshipConnection.setToY(moduleCoord.getY());
            }
        }

        //Todo make sure it is always set
        return relationshipConnection;
    }

    public void clearMap() {
        for(RecyclerView.ViewHolder viewHolder: visibleDays) {

            RelationshipMapView mapView;
            mapView = viewHolder.itemView.findViewById(R.id.overlay_relation_map);

            mapView.removeMap();

        }

    }

}
