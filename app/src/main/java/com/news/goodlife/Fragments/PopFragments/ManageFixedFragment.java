package com.news.goodlife.Fragments.PopFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.news.goodlife.Data.Local.Models.Financial.AccountModel;
import com.news.goodlife.Data.Local.Models.Financial.FixedCostModel;
import com.news.goodlife.Data.Local.Models.Financial.FixedIncomeModel;
import com.news.goodlife.Data.Local.Models.Financial.TransactionModel;
import com.news.goodlife.Interfaces.SuccessCallback;
import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

import java.util.ArrayList;
import java.util.List;

public class ManageFixedFragment extends Fragment {

    View root;
    SingletonClass singletonClass = SingletonClass.getInstance();
    SuccessCallback callback;

    View close;
    ViewGroup flexListContainer, flexSelectedContainer;
    TextView titleTV, sum;
    AsyncLayoutInflater asyncLayoutInflater;
    View parent;

    boolean Income = true;

    int TotalFixed = 0;

    List<FixedCostModel> allFixedCosts;
    List<FixedIncomeModel> allFixedIncomes;

    public ManageFixedFragment(boolean Income, View parent, SuccessCallback callback) {
        this.parent = parent;
        this.callback = callback;
        this.Income = Income;

        allFixedCosts = singletonClass.getDatabaseController().FixedCostsController.getAllFixedCosts();
        allFixedIncomes = singletonClass.getDatabaseController().FixedIncomeController.getAllFixedIncomes();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.transactions_selector, container, false);

        asyncLayoutInflater = new AsyncLayoutInflater(getContext());

        flexListContainer = root.findViewById(R.id.transactions_flex_cont);
        flexSelectedContainer = root.findViewById(R.id.selected_items_flex);
        close = root.findViewById(R.id.close);
        titleTV = root.findViewById(R.id.select_title);
        sum = root.findViewById(R.id.fixed_costs_sum);




        List<AccountModel> allAccounts = singletonClass.getDatabaseController().AccountsController.getAllAccounts();

        if(allAccounts != null){

            for(AccountModel account: allAccounts){

                List<TransactionModel> allTransactions = singletonClass.getDatabaseController().TransactionController.getAllTransactions(account.getKlarna_id());

                for(TransactionModel transaction: allTransactions){

                    filterTransactions(transaction);

                }

            }
        }
        else{

            // Tell The User To Connect an Accoont

        }


        String title;
        if(Income){
            inflateAllFixedIncomes();
            title = "Select Fixed Income";
        }
        else{
            inflateAllFixedCosts();
            title = "Select Fixed Costs";
        }

        titleTV.setText(title);

        listeners();
        return root;
    }

    private void inflateAllFixedIncomes() {

        for(FixedIncomeModel fixedIncome : allFixedIncomes){
            asyncLayoutInflater.inflate(R.layout.transaction_selector_selected_item, flexSelectedContainer, new AsyncLayoutInflater.OnInflateFinishedListener() {
                @Override
                public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {

                    allSelectedID.add(fixedIncome.getTransaction_id());
                    TotalFixed = TotalFixed + Integer.parseInt(fixedIncome.getAmount());
                    sum.setText(singletonClass.monefy(""+ TotalFixed));


                    final View button = view;
                    TextView name = view.findViewById(R.id.selected_name);
                    name.setText(fixedIncome.getName());
                    CardView deleteCard = view.findViewById(R.id.close_card);


                    deleteCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean delete = singletonClass.getDatabaseController().FixedIncomeController.deleteFixedIncome(fixedIncome.getAmount());
                            TotalFixed = TotalFixed - Integer.parseInt(fixedIncome.getAmount());
                            try{
                                sum.setText(singletonClass.monefy(""+ TotalFixed));
                            }
                            catch (Exception e){
                                sum.setText("0.00€");
                            };

                            deleteSelected(fixedIncome.getTransaction_id());
                            if(delete){
                                singletonClass.toggleFadeView(false, button, new SuccessCallback() {
                                    @Override
                                    public void success() {

                                    }

                                    @Override
                                    public void error() {

                                    }
                                });
                            }
                        }
                    });


                    flexSelectedContainer.addView(view);

                    singletonClass.toggleFadeView(true, view, new SuccessCallback() {
                        @Override
                        public void success() {

                        }

                        @Override
                        public void error() {

                        }
                    });

                }
            });


        }
    }



    private void inflateAllFixedCosts() {

        for(FixedCostModel fixedCost : allFixedCosts){
            asyncLayoutInflater.inflate(R.layout.transaction_selector_selected_item, flexSelectedContainer, new AsyncLayoutInflater.OnInflateFinishedListener() {
                @Override
                public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {

                    allSelectedID.add(fixedCost.getTransaction_id());
                    TotalFixed = TotalFixed + Integer.parseInt(fixedCost.getAmount());
                    sum.setText(singletonClass.monefy(""+ TotalFixed));


                    final View button = view;
                    TextView name = view.findViewById(R.id.selected_name);
                    name.setText(fixedCost.getName());
                    CardView deleteCard = view.findViewById(R.id.close_card);


                    deleteCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean delete = singletonClass.getDatabaseController().FixedCostsController.deleteFixedCost(fixedCost.getAmount());
                            TotalFixed = TotalFixed - Integer.parseInt(fixedCost.getAmount());
                            try{
                                sum.setText(singletonClass.monefy(""+ TotalFixed));
                            }
                            catch (Exception e){
                                sum.setText("0.00€");
                            };

                            deleteSelected(fixedCost.getTransaction_id());
                            if(delete){
                                singletonClass.toggleFadeView(false, button, new SuccessCallback() {
                                    @Override
                                    public void success() {

                                    }

                                    @Override
                                    public void error() {

                                    }
                                });
                            }
                        }
                    });


                    flexSelectedContainer.addView(view);

                    singletonClass.toggleFadeView(true, view, new SuccessCallback() {
                        @Override
                        public void success() {

                        }

                        @Override
                        public void error() {

                        }
                    });

                }
            });


        }

    }

    List<TransactionModel> allDuplicates = new ArrayList<>();
    List<TransactionModel> allUnique = new ArrayList<>();


    private void listeners() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singletonClass.toggleFadeView(false, parent, new SuccessCallback() {
                    @Override
                    public void success() {
                        //TODO maybe destroy the Fragment if Neccessary !!Look into That!!
                        callback.success();

                    }

                    @Override
                    public void error() {

                    }
                });
            }
        });
    }

    private void filterTransactions(TransactionModel transaction){

        //TODO Check the ----Logic---- for Duplicates --- For now we Assume they are all Unique
        if(Income){
            //Get Credit Transactions
            if(transaction.getType().equals("CREDIT")){
                allUnique.add(transaction);
                inflateTransaction(transaction);
            }
        }
        else{
            //Get Debit Transactions
            if(transaction.getType().equals("DEBIT")){
                allUnique.add(transaction);
                inflateTransaction(transaction);
            }
        }
    }

    List<String> allSelectedID = new ArrayList<>();

    private void inflateTransaction(TransactionModel transaction){

        asyncLayoutInflater.inflate(R.layout.transaction_selector_listitem, flexListContainer, new AsyncLayoutInflater.OnInflateFinishedListener() {
            @Override
            public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {
                TextView amount, desc;

                amount = view.findViewById(R.id.transaction_amount);
                desc = view.findViewById(R.id.desc_cont);

                String cash = singletonClass.monefy(transaction.getAmount());

                amount.setText(cash);
                //TODO Change this to CounterParty
                desc.setText(transaction.getReference());

                flexListContainer.addView(view);


                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //check if selected

                        if(!checkSelected(transaction.getTransaction_id())){

                            //It is not selected now we inflate the Data
                            inflateSelected(transaction);
                            if(Income){
                                saveFixedIncome(transaction);
                            }
                            else{
                                saveFixedCost(transaction);
                            }

                            allSelectedID.add(transaction.getTransaction_id());
                            TotalFixed = TotalFixed + Integer.parseInt(transaction.getAmount());
                            sum.setText(singletonClass.monefy(""+ TotalFixed));

                        }

                    }
                });
            }
        });

    }

    private void saveFixedIncome(TransactionModel transaction) {

        FixedIncomeModel fixedIncome = new FixedIncomeModel();
        //TODO MAKE SURE THE IBAN FROM THE COUNTERPARTY IS INSIDE
        fixedIncome.setIban(transaction.getAmount());
        fixedIncome.setName(transaction.getReference());
        fixedIncome.setTransaction_id(transaction.getTransaction_id());
        fixedIncome.setStart_date("null");
        fixedIncome.setEnd_date("null");
        fixedIncome.setExpected(transaction.getDate());
        fixedIncome.setAmount(transaction.getAmount());

        singletonClass.getDatabaseController().FixedIncomeController.addFixedIncome(fixedIncome);

    }

    private void saveFixedCost(TransactionModel transaction) {

        FixedCostModel fixedCost = new FixedCostModel();
        //TODO MAKE SURE THE IBAN FROM THE COUNTERPARTY IS INSIDE
        fixedCost.setIban(transaction.getAmount());
        fixedCost.setName(transaction.getReference());
        fixedCost.setTransaction_id(transaction.getTransaction_id());
        fixedCost.setStart_date("null");
        fixedCost.setEnd_date("null");
        fixedCost.setExpected(transaction.getDate());
        fixedCost.setAmount(transaction.getAmount());

        singletonClass.getDatabaseController().FixedCostsController.addFixedCost(fixedCost);
    }

    private void deleteFixedCost(TransactionModel transaction){

        singletonClass.getDatabaseController().FixedCostsController.deleteFixedCost(transaction.getAmount());

    }

    private void inflateSelected(TransactionModel transaction) {

        asyncLayoutInflater.inflate(R.layout.transaction_selector_selected_item, flexSelectedContainer, new AsyncLayoutInflater.OnInflateFinishedListener() {
            @Override
            public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {

                final View button = view;
                TextView name = view.findViewById(R.id.selected_name);
                name.setText(transaction.getReference());
                CardView deleteCard = view.findViewById(R.id.close_card);


                deleteCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean delete = singletonClass.getDatabaseController().FixedCostsController.deleteFixedCost(transaction.getAmount());
                        TotalFixed = TotalFixed - Integer.parseInt(transaction.getAmount());
                        try{
                            sum.setText(singletonClass.monefy(""+ TotalFixed));
                        }
                        catch (Exception e){
                            sum.setText("0.00€");
                        };

                        deleteSelected(transaction.getTransaction_id());
                        if(delete){
                         singletonClass.toggleFadeView(false, button, new SuccessCallback() {
                             @Override
                             public void success() {

                             }

                             @Override
                             public void error() {

                             }
                         });
                        }
                    }
                });


                flexSelectedContainer.addView(view);

                singletonClass.toggleFadeView(true, view, new SuccessCallback() {
                    @Override
                    public void success() {

                    }

                    @Override
                    public void error() {

                    }
                });

            }
        });
    }

    private boolean checkSelected(String transaction_id) {

        for(String id : allSelectedID){

            if(transaction_id.equals(id)){
                return true;
            }
        }
        return false;
    }

    private boolean deleteSelected(String transaction_id){

        int i = 0;
        for(String id : allSelectedID){

            if(id.equals(transaction_id)){
                allSelectedID.remove(i);
                return true;
            }
            i++;
        }
        return false;

    }
}
