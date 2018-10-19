package com.mad.sparkle.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

public class RegisterViewModel extends ViewModel {

    public ObservableField<String> email = new ObservableField<String>();
    public ObservableField<String> password = new ObservableField<String>();
    public ObservableField<String> firstName = new ObservableField<String>();
    public ObservableField<String> lastName = new ObservableField<String>();
    public ObservableField<String> mobilePhone = new ObservableField<String>();

    public ObservableField<String> emailError = new ObservableField<String>();
    public ObservableField<String> passwordError = new ObservableField<String>();
    public ObservableField<String> firstNameError = new ObservableField<String>();
    public ObservableField<String> lastNameError = new ObservableField<String>();
    public ObservableField<String> mobilePhoneError = new ObservableField<String>();



}
