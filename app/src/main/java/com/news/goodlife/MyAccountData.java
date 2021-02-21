package com.news.goodlife;

import com.news.goodlife.Data.Local.Models.Financial.TransactionModel;

import java.util.ArrayList;
import java.util.List;

public class MyAccountData {

    List<TransactionModel> allTransactions = new ArrayList<>();
    String Account_ID;

    public MyAccountData(String account_ID) {
        Account_ID = account_ID;

        // Generate Account Data
    }

    private TransactionModel getTransaction(String account_id, String datum, String reference, String summe, String debit) {
        TransactionModel transaction = new TransactionModel();
        transaction.setAccount_id(account_id);
        transaction.setTransaction_id("null");
        transaction.setReference(reference);
        transaction.setCounterparty_id("null");
        transaction.setDate(datum);
        transaction.setValue_date(datum);
        transaction.setState(debit);
        transaction.setType(debit);
        transaction.setMethod(debit);
        transaction.setAmount(summe);
        transaction.setBooking_date(datum);
        transaction.setAmount_currency("EUR");

        return transaction;
    }

    public List<TransactionModel> getMyTestTransactions(){
        allTransactions.add(getTransaction(Account_ID,"2020-01-14","Spardabank","10000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-01-15","Bargeldabhebung","342","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-01-16","Flixbus - Berlin","899","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-01-16","Moneygram","5199","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-01-17","Google Payment","114","CREDIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-01-17","Persona","44614","CREDIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-01-18","SSB","397","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-01-18","Aufladen.de","1099","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-01-18","Überweisung Kartengebühren","60","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-01-18","ksp Rechtsanwalt","2100","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-01-20","Amazon Prime","1172","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-01-20","Adobe Aftereffects","2379","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-01-20","Google Youtube","399","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-01-20","Netflix","1599","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-01-20","Moneygram","10119","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-01-20","BW Bank","16500","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-01-20","SSB","397","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-01-20","Kaufland","977","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-01-21","Stuttgart Abfall","6900","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-01-21","Bargeld Abhebung","472","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-01-30","Bargeld Abhebung","28","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-01-30","Jahresentgeld","6800","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-01-14","Persona","92500","CREDIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-01-31","Playstation Network","899","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-01-31","Stars Douglas","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-01-31","Michael Bruecken K","700","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-01-31","Penny Sorpe","670","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-01-31","AMZNPrime","799","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-01","Monatliche Kontofunktionen","500","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-01","SSB","275","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-01","Spotify","999","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-01","Überweisung","60","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-01","Fabian Feyer","345","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-01","Spardabank","10000","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-02","Überweisung","60","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-02","Joachim Mossel","1900","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-03","Kaufland","17925","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-03","Adobe After Effects","1190","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-03","AWS","4637","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-03","1u1","2400","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-03","1u1","2400","CREDIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-04","Stars Douglas","1500","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-05","Worldpay","22500","CREDIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-07","Prime Video","399","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-07","Stars Douglas","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-07","Electraworks","1500","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-07","Electraworks","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-07","Electraworks","2500","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-07","Electraworks","1000","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-08","Stars Douglas","1500","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-10","Stars Douglas","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-10","Stars Douglas","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-10","Kaufland","87","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-10","Stars Douglas","1000","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-11","Stars Douglas","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-11","Stars Douglas","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-11","BAT Langen","9000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-11","Aufladen","1099","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-11","Bargeld Automat","500","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-11","PXP Financial Limited","1500","CREDIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-12","Electraworks","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-12","Electraworks","2000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-12","Electraworks","2000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-12","Electraworks","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-12","Electraworks","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-12","Fabian Feyer","72500","CREDIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-13","Volksbank Sauerland","3000","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-14","Bargeld Automat","500","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-15","SSB","275","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-17","Amazon Prime","1200","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-17","SSB","275","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-18","Penny Sorpe","168","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-18","Penny Sorpe","1804","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-18","Überweisung","60","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-18","ksp Rechtsanwalt","2100","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-18","Persona","925","CREDIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-18","Überweisung","60","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-18","Moemax","145000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-18","Überweisung","60","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-18","infoscore","13072","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-19","Stars Douglas","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-19","DB BAHN","5990","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-19","Volksbank Sauerland","3000","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-20","Bargeld Automat","486","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-20","Bargeld Automat","14","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-20","Lucy Hahn","125000","CREDIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-21","Stars Douglas","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-21","Netflix","1599","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-22","Penny Sorpe","2507","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-22","Moneygram","10199","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-22","Volksbank Sauerland","10000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-22","Stars","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-22","Aral Station","1025","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-23","Bargled Abhebung","5000","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-24","Stars Douglas","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-24","Aufladen.de","2099","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-24","Aufladen.de","1099","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-24","Moneygram","7199","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-24","Volksbank","5000","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-25","Bargeld Abhebung","500","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-27","Penny Sorpe","605","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-27","Stars Douglas","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-27","Stars Douglas","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-27","Aufladen","2099","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-27","Persona","925","CREDIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-28","Moneygram","45399","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-28","Penny Sorpe","536","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-02-29","Stars Douglas","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-29","Sparkasse Arnsberg","5000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-29","Spotify","999","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-29","Gut Funkenhof","590","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-29","Aufladen","1099","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-29","Aral Station","999","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-02-29","AMZNPrime","799","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-03-01","Bargeld Abhebungen","500","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-01","Monatlche Kontofunktion","500","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-01","Fabian Feyer","44500","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-01","Überweisung Kartengebühren","60","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-03-02","Playstation Network","899","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-02","Stars Douglas","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-02","Aufladen.de","2099","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-02","WorldRemit","20599","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-02","Stars Douglas","15000","CREDIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-02","Stars Douglas","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-02","Moneygram","399","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-02","SSB","397","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-03-03","Aufladen.de","1099","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-03","Aufladen.de","1099","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-03-04","AWS","3235","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-04","Stars Douglas","1099","CREDIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-04","Bietigheim","25000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-04","Bargeld Automat","500","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-04","WorldRemit","3000","CREDIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-03-05","Elektraworks","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-05","Elektraworks","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-05","Elektraworks","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-05","Elektraworks","2000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-05","Elektraworks","1000","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-03-06","Aufladen","2099","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-03-07","Prime Video","399","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-07","SSB","397","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-07","Aufladen.de","1099","DEBIT"));


        allTransactions.add(getTransaction(Account_ID,"2020-03-09","Stars Douglas","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-09","Kaufland","11812","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-09","Oring Ltd","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-09","Oring Ltd","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-09","Oring Ltd","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-09","Stars Douglas","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-09","Stars Douglas","1000","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-03-10","Poerstars","1000","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-03-11","SSB","275","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-03-14","SSB","275","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-03-17","Persona","104495","CREDIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-17","Überweisung Kartengebühren","60","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-17","Christine Rau","30000","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-03-18","Stars Douglas","2000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-18","Stars Douglas","2000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-18","Stars DOuglas","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-18","SPK Arnsberg","30000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-18","Überweisung aus Kartenguthaben","60","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-18","ksp Rechtsanwaelte","2100","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-03-19","Moneygram","30239","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-19","Bargeldabhebung am Automat","500","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-19","Amazon Prime","1198","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-19","Netflix","1599","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-19","Stars Douglas","1000","CREDIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-19","McDonalds Sundern","1376","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-03-20","Worldpay","1000","CREDIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-03-21","Aufladen.de","2000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-21","Stars","2000","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-03-23","Stars Douglas","1500","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-03-30","Persona","92500","CREDIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-30","Stichting Degiro","1","CREDIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-30","Stichting Degiro","76","CREDIT"));

        allTransactions.add(getTransaction(Account_ID,"2020-03-31","Playstation Network","899","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-31","Penny Sorpe","1128","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2020-03-31","AMZNPrime","799","DEBIT"));



        //2021

        allTransactions.add(getTransaction(Account_ID,"2021-01-01","AMZNPrime","799","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-01","Monatliche Kontoführungsgebühren","500","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-02","WorldRemit","4099","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-02","AWS EMEA","3805","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-02","SSB","397","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-05","Überweisung aus Kartenguthaben","60","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-05","Dampforado","3115","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-06","PayPal","599","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-07","Lidl","5169","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-07","Prime Video","399","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-09","PayPal","4350","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-12","Penny","715","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-12","Zalando","10695","CREDIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-12","Überweisung Kartenguthaben","60","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-12","WorldRemit","5099","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-13","PayPal","2000","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-14","Penny","1076","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-15","PayPal","4860","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-16","Michael Bruecken K","4107","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-18","Disney Plus","699","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-18","PayPal","2000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-18","Google Payments","115","CREDIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-19","Überweisung Kartenguthaben","60","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-19","WorldRemit","12099","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-19","Persona","173350","CREDIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-20","Netflix","1599","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-20","SPK Arnsberg","15000","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-21","PayPal","1000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-21","GOOGLE","199","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-21","Bargeldabhebung Automat","500","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-21","E-Plus","999","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-21","E-Plus","999","CREDIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-22","PayPal","3650","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-23","Michael Brueckeen K","4786","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-23","Jahresentgelt","6800","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-23","PayPal","7390","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-24","Überweisung Kartenguthaben","60","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-24","DB Vertrieb","999","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-25","AMZNPrime","20","CREDIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-26","Google Pay","549","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-27","Kaufland","17873","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-28","Google Stadia","999","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-28","Überweisung aus Kartenguthaben","60","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-28","Christine Rau","113702","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-28","Überweisung aus Kartneguthaben","60","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-28","1und1 IONOS","2519","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-01-28","Persona","92500","CREDIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-01-30","PayPal","1299","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-02-01","AMZNPRIME","799","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-02-01","Monatliche Kontoführung","500","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-02-01","Überweisung aus Kartenguthaben","60","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-02-01","WorldRemit","15099","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-02-01","Überweisung Kartenguthaben","60","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-02-01","Fabian Feyer","370","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-02-02","SSB","11800","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-02-03","AWS","4622","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-02-05","AMZN MKTP","3995","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-02-07","Überweisung Kartneguthaben","60","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-02-07","PPPRO Payment","15399","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-02-08","PayPal","1500","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-02-08","PayPal","1500","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-02-15","Kaufland","14425","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-02-16","PayPal","2000","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-02-16","Persona","125826","CREDIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-02-16","Überweisung aus Kartenguthabben","60","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-02-16","PPRO Payment","30399","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-02-17","Disney PLus","699","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-02-17","Überweisung aus Kartenguthaben","60","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-02-17","PPRO Payment","30399","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-02-17","SPARDA BANK","7000","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-02-18","Bargeldabhebung am Automat","500","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-02-18","CRO Internet","11619","DEBIT"));

        allTransactions.add(getTransaction(Account_ID,"2021-02-19","Netflix","1599","DEBIT"));
        allTransactions.add(getTransaction(Account_ID,"2021-02-19","PPRO Payment","30399","CREDIT"));

        return allTransactions;
    }

}
