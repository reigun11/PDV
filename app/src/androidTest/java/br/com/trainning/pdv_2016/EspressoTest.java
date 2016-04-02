package br.com.trainning.pdv_2016;

/**
 * Created by elcio on 27/03/16.
 */
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.format.DateUtils;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import br.com.trainning.pdv_2016.domain.model.Produto;
import br.com.trainning.pdv_2016.domain.network.APIClient;
import br.com.trainning.pdv_2016.ui.CadastroNovoActivity;
import br.com.trainning.pdv_2016.ui.MainActivity;
import se.emilsjolander.sprinkles.Query;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

@RunWith(AndroidJUnit4.class)
public class EspressoTest {

    private final String TESTE_DESCRICAO="Produto Teste";
    private final String TESTE_UNIDADE="pc";
    private final String TESTE_PRECO="4.83";
    private final String TESTE_CODIGO_BARRA="789000000000";
    private final String TESTE_FOTO="foto";
    private final String TESTE_DESCRICAO_REPLACE="Produto Teste Editado";
    private final String TESTE_UNIDADE_REPLACE="un";
    private final String TESTE_PRECO_REPLACE="5.55";



    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void antesDeTestar(){
        Produto produto = Query.one(Produto.class,"select * from produto where codigo_barra = ?",TESTE_CODIGO_BARRA).get();
        if(produto!=null) {
            produto.delete();
        }

        try {
          String response =  new APIClient().getRestService().deleteProduto(TESTE_CODIGO_BARRA);
            Log.d("RETROFIT:","message:"+response);

        }catch(Exception e){
            Log.e("ERROR:",e.getMessage());
        }


    }

    @Test
    public void cadastroNovoProduto(){

        long waitingTime = DateUtils.SECOND_IN_MILLIS * 3;

        // Make sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(
                waitingTime * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(
                waitingTime * 2, TimeUnit.MILLISECONDS);

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        // Click the item.
        onView(withText("Novo Produto"))
                .perform(click());

        onView(withId(getResourceId("editTextDescricao")))
                .perform(typeText(TESTE_DESCRICAO));
        onView(withId(getResourceId("editTextUnidade")))
                .perform(typeText(TESTE_UNIDADE));
        onView(withId(getResourceId("editTextPreco")))
                .perform(typeText(TESTE_PRECO));
        onView(withId(getResourceId("editTextCodigo")))
                .perform(typeText(TESTE_CODIGO_BARRA), closeSoftKeyboard());


        onView(withId(getResourceId("fab"))).perform(click());


        // Now we wait
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(waitingTime);
        Espresso.registerIdlingResources(idlingResource);

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        // Click the item.
        onView(withText("Editar produto"))
                .perform(click());

        onView(withId(getResourceId("spinner"))).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(TESTE_CODIGO_BARRA))).perform(click());
        //onView(withId(spinnerId)).check(matches(withSpinnerText(containsString(selectionText))));

        onView(withId(getResourceId("editTextDescricao"))).check(matches(withText(TESTE_DESCRICAO)));
        onView(withId(getResourceId("editTextUnidade"))).check(matches(withText(TESTE_UNIDADE)));
        onView(withId(getResourceId("editTextPreco"))).check(matches(withText(TESTE_PRECO)));
        onView(withId(getResourceId("editTextCodigo"))).check(matches(withText(TESTE_CODIGO_BARRA)));

        onView(withId(getResourceId("editTextDescricao"))).perform(clearText(), typeText(TESTE_DESCRICAO_REPLACE));
        onView(withId(getResourceId("editTextUnidade"))).perform(clearText(), typeText(TESTE_UNIDADE_REPLACE));
        onView(withId(getResourceId("editTextPreco"))).perform(clearText(), typeText(TESTE_PRECO_REPLACE));


        onView(withId(getResourceId("fab"))).perform(click());

        Espresso.unregisterIdlingResources(idlingResource);
        // Now we wait
        IdlingResource idlingResource2 = new ElapsedTimeIdlingResource(waitingTime);
        Espresso.registerIdlingResources(idlingResource2);

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        // Click the item.
        onView(withText("Editar produto"))
                .perform(click());

        onView(withId(getResourceId("spinner"))).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(TESTE_CODIGO_BARRA))).perform(click());

        onView(withId(getResourceId("editTextDescricao"))).check(matches(withText(TESTE_DESCRICAO_REPLACE)));
        onView(withId(getResourceId("editTextUnidade"))).check(matches(withText(TESTE_UNIDADE_REPLACE)));
        onView(withId(getResourceId("editTextPreco"))).check(matches(withText(TESTE_PRECO_REPLACE)));
        onView(withId(getResourceId("editTextCodigo"))).check(matches(withText(TESTE_CODIGO_BARRA)));

        Espresso.pressBack();


        Espresso.unregisterIdlingResources(idlingResource2);

    }

    private static int getResourceId(String s) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        String packageName = targetContext.getPackageName();
        return targetContext.getResources().getIdentifier(s, "id", packageName);
    }
}