package xyz.android.pc4_calderon;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private RadioGroup categoryRadioGroup;
    private RadioGroup languageRadioGroup;
    private Button getJokeButton;
    private TextView jokeResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoryRadioGroup = findViewById(R.id.categoryRadioGroup);
        languageRadioGroup = findViewById(R.id.languageRadioGroup);
        getJokeButton = findViewById(R.id.getJokeButton);
        jokeResultTextView = findViewById(R.id.jokeResultTextView);

        getJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedCategoryId = categoryRadioGroup.getCheckedRadioButtonId();
                int selectedLanguageId = languageRadioGroup.getCheckedRadioButtonId();

                String category = "";
                if (selectedCategoryId == R.id.anyCategoryRadioButton) {
                    category = "Any";
                } else if (selectedCategoryId == R.id.programmingCategoryRadioButton) {
                    category = "Programming";
                }

                String language = "";
                if (selectedLanguageId == R.id.englishLanguageRadioButton) {
                    language = "en";
                } else if (selectedLanguageId == R.id.spanishLanguageRadioButton) {
                    language = "es";
                }

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://v2.jokeapi.dev/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                jokeAPI jokeApiService = retrofit.create(jokeAPI.class);

                Call<joke> call = jokeApiService.getJoke(category, language);
                call.enqueue(new Callback<joke>() {
                    @Override
                    public void onResponse(Call<joke> call, Response<joke> response) {
                        if (response.isSuccessful()) {
                            joke joke = response.body();

                            String result = "";
                            if (joke != null) {
                                result += "Categoría: " + joke.getCategory() + "\n";

                                result += "Tipo: " + joke.getType() + "\n";

                                if (joke.getType().equals("single")) {
                                    result += joke.getJoke();
                                } else if (joke.getType().equals("twopart")) {
                                    result += joke.getSetup() + "\n" + joke.getDelivery();
                                }
                            }

                            jokeResultTextView.setText(result);
                        } else {
                            jokeResultTextView.setText("Error al obtener el chiste.");
                        }
                    }

                    @Override
                    public void onFailure(Call<joke> call, Throwable t) {
                        jokeResultTextView.setText("Error de conexión.");
                    }
                });
            }
        });
    }
}