package com.example.jsonparseexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final String TAG = "Demo";
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPerson();
    }

        void getPerson () {
            Log.d(TAG, "getContact: start");
            Request request = new Request.Builder()
                    .url("https://www.theappsdr.com/persons/json")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Log.d(TAG, "onResponse: Thread Id" + Thread.currentThread().getId());
                    if (response.isSuccessful()) {
                        try {
                            ArrayList<Person> persons = new ArrayList<>();

                            JSONObject json = new JSONObject(response.body().string());
                            JSONArray personsJson = json.getJSONArray("persons");

                            for (int i = 0; i < personsJson.length(); i++) {
                                JSONObject personJsonObject = personsJson.getJSONObject(i);
                                Person person = new Person();
                                person.setName(personJsonObject.getString("name"));
                                person.setId(personJsonObject.getString("id"));
                                person.setAge(personJsonObject.getString("age"));

                                JSONObject addressJson = personJsonObject.getJSONObject("address");
                                Address address = new Address();
                                address.setLine1(addressJson.getString("line1"));
                                address.setCity(addressJson.getString("city"));
                                address.setState(addressJson.getString("state"));
                                address.setZip(addressJson.getString("zip"));
                                person.setAddress(address);

                                persons.add(person);
                            }

//                            Person person = new Person();
//                            person.setName(json.getString("name"));
//                            person.setId(json.getString("id"));
//                            person.setAge(json.getString("age"));
//
//                            JSONObject addressJson = json.getJSONObject("address");
//                            Address address = new Address();
//                            address.setLine1(addressJson.getString("line1"));
//                            address.setCity(addressJson.getString("city"));
//                            address.setState(addressJson.getString("state"));
//                            address.setZip(addressJson.getString("zip"));
//                            person.setAddress(address);

                            Log.d(TAG, "onResponse: " + persons);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } else {
                        ResponseBody responseBody = response.body();
                        String body = responseBody.string();
                        Log.d(TAG, "onResponse: " + body);
                    }
                }
            });

            Log.d(TAG, "getContact: end");
        }

    }