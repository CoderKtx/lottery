package com.raffleclub.app.ui.priceslots;

import com.raffleclub.app.model.Contest;
import com.raffleclub.app.repository.PriceSoltRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PrizePoolViewModel extends ViewModel {

    private MutableLiveData<List<Contest>> arrayInvoiceMutableLiveData;

    void init(String ID) {
        if (arrayInvoiceMutableLiveData != null) {
            return;
        }
        PriceSoltRepository newsRepository = PriceSoltRepository.getInstance();
        arrayInvoiceMutableLiveData = newsRepository.getNews(ID);
    }

    LiveData<List<Contest>> getDomesticList() {
        return arrayInvoiceMutableLiveData;
    }
}
