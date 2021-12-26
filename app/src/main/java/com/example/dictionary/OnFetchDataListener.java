package com.example.dictionary;

import com.example.dictionary.Models.APIrespone;

public interface OnFetchDataListener {
    void onFetchData(APIrespone apiRespone, String message);
    void OnError(String message);
}
