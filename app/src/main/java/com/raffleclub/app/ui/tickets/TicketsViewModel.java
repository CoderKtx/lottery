package com.raffleclub.app.ui.tickets;

import com.raffleclub.app.model.Contest;
import com.raffleclub.app.repository.BronzeRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TicketsViewModel extends ViewModel {

    private MutableLiveData<List<Contest>> arrayInvoiceMutableLiveData;

    void init(String contestId, String pkgId) {
//        if (arrayInvoiceMutableLiveData != null) {
//            return;
//        }
        BronzeRepository newsRepository = BronzeRepository.getInstance();
        arrayInvoiceMutableLiveData = newsRepository.getNews(contestId, pkgId);
    }

    LiveData<List<Contest>> getDomesticList() {
        return arrayInvoiceMutableLiveData;
    }
}
