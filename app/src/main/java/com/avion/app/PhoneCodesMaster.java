package com.avion.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import androidx.paging.PositionalDataSource;

import com.avion.app.entity.CountryEntity;
import com.avion.app.unit.NekRepository;
import com.avion.app.unit.NekViewModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class PhoneCodesMaster extends NekRepository {

    LiveData<PagedList<CountryEntity>> live_countries;

    public CountryEntity Afghanistan;
    public CountryEntity Albania;
    public CountryEntity Algeria;
    public CountryEntity American_Samoa;
    public CountryEntity Andorra;
    public CountryEntity Angola;
    public CountryEntity Anguilla;
    public CountryEntity Antigua_and_Barbuda;
    public CountryEntity Armenia;
    public CountryEntity Argentina;
    public CountryEntity Australia;
    public CountryEntity Austria;
    public CountryEntity Azerbaijan;
    public CountryEntity Bahamas;
    public CountryEntity Bahrain;
    public CountryEntity Bangladesh;
    public CountryEntity Barbados;
    public CountryEntity Belarus;
    public CountryEntity Belgium;
    public CountryEntity Belize;
    public CountryEntity Benin;
    public CountryEntity Bermuda;
    public CountryEntity Bolivia;
    public CountryEntity Bosnia_and_Herzegovina;
    public CountryEntity Botswana;
    public CountryEntity Brazil;
    public CountryEntity British_Virgin_Islands;
    public CountryEntity Brunei_Darusalaam;
    public CountryEntity Bulgaria;
    public CountryEntity Burkina_Faso;
    public CountryEntity Burundi;
    public CountryEntity Byelorussian;
    public CountryEntity Cambodia;
    public CountryEntity Cameroon;
    public CountryEntity Canada;
    public CountryEntity Cape_Verde;
    public CountryEntity Cayman_Islands;
    public CountryEntity Central_African_Republic;
    public CountryEntity Chad;
    public CountryEntity Chile;
    public CountryEntity China;
    public CountryEntity Christmas_Island;
    public CountryEntity Cocos_Islands;
    public CountryEntity Colombia;
    public CountryEntity Commonwealth_of_the_Northern_Mariana_Islands;
    public CountryEntity Comoros_and_Mayotte_Island;
    public CountryEntity Congo;
    public CountryEntity Cook_Islands;
    public CountryEntity Costa_Rica;
    public CountryEntity Croatia;
    public CountryEntity Cuba;
    public CountryEntity Cyprus;
    public CountryEntity Czech_Republic;
    public CountryEntity Denmark;
    public CountryEntity Diego_Garcia;
    public CountryEntity Djibouti;
    public CountryEntity Dominica;
    public CountryEntity Dominican_Republic;
    public CountryEntity East_Timor;
    public CountryEntity Ecuador;
    public CountryEntity Egypt;
    public CountryEntity El_Salvador;
    public CountryEntity Equatorial_Guinea;
    public CountryEntity Estonia;
    public CountryEntity Ethiopia;
    public CountryEntity Faeroe_Islands;
    public CountryEntity Falkland_Islands;
    public CountryEntity Fiji;
    public CountryEntity Finland;
    public CountryEntity France;
    public CountryEntity French_Antilles;
    public CountryEntity French_Guiana;
    public CountryEntity French_Polynesia;
    public CountryEntity Gabon;
    public CountryEntity Gambia;
    public CountryEntity Georgia;
    public CountryEntity Germany;
    public CountryEntity Ghana;
    public CountryEntity Gibraltar;
    public CountryEntity Greece;
    public CountryEntity Greenland;
    public CountryEntity Grenada;
    public CountryEntity Guam;
    public CountryEntity Guatemala;
    public CountryEntity Guinea;
    public CountryEntity Guinea_Bissau;
    public CountryEntity Guyana;
    public CountryEntity Haiti;
    public CountryEntity Honduras;
    public CountryEntity Hong_Kong;
    public CountryEntity Hungary;
    public CountryEntity Iceland;
    public CountryEntity India;
    public CountryEntity Indonesia;
    public CountryEntity Iran;
    public CountryEntity Iraq;
    public CountryEntity Irish_Republic;
    public CountryEntity Israel;
    public CountryEntity Italy;
    public CountryEntity Ivory_Coast;
    public CountryEntity Jamaica;
    public CountryEntity Japan;
    public CountryEntity Jordan;
    public CountryEntity Kazakhstan;
    public CountryEntity Kenya;
    public CountryEntity Kiribati_Republic;
    public CountryEntity Kirgizia;
    public CountryEntity Kuwait;
    public CountryEntity Laos;
    public CountryEntity Latvia;
    public CountryEntity Lebanon;
    public CountryEntity Lesotho;
    public CountryEntity Liberia;
    public CountryEntity Libya;
    public CountryEntity Liechtenstein;
    public CountryEntity Lithuania;
    public CountryEntity Luxembourg;
    public CountryEntity Macao;
    public CountryEntity Macedonia;
    public CountryEntity Madagascar;
    public CountryEntity Malawi;
    public CountryEntity Malaysia;
    public CountryEntity Maldives;
    public CountryEntity Mali;
    public CountryEntity Malta;
    public CountryEntity Marshall_Islands;
    public CountryEntity Martinique;
    public CountryEntity Mauritania;
    public CountryEntity Mauritius;
    public CountryEntity Mexico;
    public CountryEntity Micronesia;
    public CountryEntity Monaco;
    public CountryEntity Montenegro;
    public CountryEntity Mongolia;
    public CountryEntity Montserrat;
    public CountryEntity Morocco;
    public CountryEntity Mozambique;
    public CountryEntity Myanmar;
    public CountryEntity Namibia;
    public CountryEntity Nauru;
    public CountryEntity Nepal;
    public CountryEntity Netherlands;
    public CountryEntity Netherlands_Antilles;
    public CountryEntity New_Caledonia;
    public CountryEntity New_Zealand;
    public CountryEntity Nicaragua;
    public CountryEntity Niger;
    public CountryEntity Nigeria;
    public CountryEntity Niue;
    public CountryEntity Norfolk_Island;
    public CountryEntity North_Korea;
    public CountryEntity North_Yemen;
    public CountryEntity Northern_Mariana_Islands;
    public CountryEntity Norway;
    public CountryEntity Oman;
    public CountryEntity Pakistan;
    public CountryEntity Panama;
    public CountryEntity Papua_New_Guinea;
    public CountryEntity Paraguay;
    public CountryEntity Peru;
    public CountryEntity Philippines;
    public CountryEntity Poland;
    public CountryEntity Portugal;
    public CountryEntity Puerto_Rico;
    public CountryEntity Qatar;
    public CountryEntity Republic_of_San_Marino;
    public CountryEntity Reunion;
    public CountryEntity Romania;
    public CountryEntity Russia;
    public CountryEntity Rwandese_Republic;
    public CountryEntity Saint_Helena_and_Ascension_Island;
    public CountryEntity Saint_Pierre_et_Miquelon;
    public CountryEntity San_Marino;
    public CountryEntity Sao_Tome_e_Principe;
    public CountryEntity Saudi_Arabia;
    public CountryEntity Senegal;
    public CountryEntity Seychelles;
    public CountryEntity Sierra_Leone;
    public CountryEntity Singapore;
    public CountryEntity Slovakia;
    public CountryEntity Slovenia;
    public CountryEntity Solomon_Islands;
    public CountryEntity Somalia;
    public CountryEntity South_Africa;
    public CountryEntity South_Korea;
    public CountryEntity South_Yemen;
    public CountryEntity Spain;
    public CountryEntity Sri_Lanka;
    public CountryEntity St_Kitts_and_Nevis;
    public CountryEntity St_Lucia;
    public CountryEntity St_Vincent_and_the_Grenadines;
    public CountryEntity Sudan;
    public CountryEntity Suriname;
    public CountryEntity Svalbard_and_Jan_Mayen_Islands;
    public CountryEntity Swaziland;
    public CountryEntity Sweden;
    public CountryEntity Switzerland;
    public CountryEntity Syria;
    public CountryEntity Tajikistan;
    public CountryEntity Taiwan;
    public CountryEntity Tanzania;
    public CountryEntity Thailand;
    public CountryEntity Togolese_Republic;
    public CountryEntity Tokelau;
    public CountryEntity Tonga;
    public CountryEntity Trinidad_and_Tobago;
    public CountryEntity Tunisia;
    public CountryEntity Turkey;
    public CountryEntity Turkmenistan;
    public CountryEntity Turks_And_Caicos_Islands;
    public CountryEntity Tuvalu;
    public CountryEntity US_Virgin_Islands;
    public CountryEntity Uganda;
    public CountryEntity Ukraine;
    public CountryEntity United_Arab_Emirates;
    public CountryEntity United_Kingdom;
    public CountryEntity Uruguay;
    public CountryEntity USA;
    public CountryEntity Uzbekistan;
    public CountryEntity Vanuatu;
    public CountryEntity Vatican_City;
    public CountryEntity Venezuela;
    public CountryEntity Vietnam;
    public CountryEntity Wales;
    public CountryEntity Western_Sahara;
    public CountryEntity Western_Samoa;
    public CountryEntity Yugoslavia;
    public CountryEntity Zaire;
    public CountryEntity Zambia;
    public CountryEntity Zimbabwe;
    ArrayList<CountryEntity> all_countries;
    Application ap;
    Context conn;
    NekViewModel nm;

    public PhoneCodesMaster(Context cont, Application application, NekViewModel nekViewModel) {
        super(application, nekViewModel);
        Resources res = cont.getResources();
        this.ap = application;
        this.conn = cont;
        this.nm = nekViewModel;
        Afghanistan = new CountryEntity(res.getString(R.string.Afghanistan), "+93");
        Albania = new CountryEntity(res.getString(R.string.Albania), "+355");
        Algeria = new CountryEntity(res.getString(R.string.Algeria), "+21");
        American_Samoa = new CountryEntity(res.getString(R.string.American_Samoa), "+684");
        Andorra = new CountryEntity(res.getString(R.string.Andorra), "+376");
        Angola = new CountryEntity(res.getString(R.string.Angola), "+244");
        Anguilla = new CountryEntity(res.getString(R.string.Anguilla), "+1-264");
        Antigua_and_Barbuda = new CountryEntity(res.getString(R.string.Antigua_and_Barbuda), "+1-268");
        Armenia = new CountryEntity(res.getString(R.string.Armenia), "+374");
        Argentina = new CountryEntity(res.getString(R.string.Argentina), "+54");
        Australia = new CountryEntity(res.getString(R.string.Australia), "+61");
        Austria = new CountryEntity(res.getString(R.string.Austria), "+43");
        Azerbaijan = new CountryEntity(res.getString(R.string.Azerbaijan), "+994");
        Bahamas = new CountryEntity(res.getString(R.string.Bahamas), "+1-242");
        Bahrain = new CountryEntity(res.getString(R.string.Bahrain), "+973");
        Bangladesh = new CountryEntity(res.getString(R.string.Bangladesh), "+880");
        Barbados = new CountryEntity(res.getString(R.string.Barbados), "+1-246");
        Belarus = new CountryEntity(res.getString(R.string.Belarus), "+375");
        Belgium = new CountryEntity(res.getString(R.string.Belgium), "+32");
        Belize = new CountryEntity(res.getString(R.string.Belize), "+501");
        Benin = new CountryEntity(res.getString(R.string.Benin), "+229");
        Bermuda = new CountryEntity(res.getString(R.string.Bermuda), "+1-441");
        Montenegro = new CountryEntity(res.getString(R.string.Montenegro), "+382");
        Bolivia = new CountryEntity(res.getString(R.string.Bolivia), "+591");
        Bosnia_and_Herzegovina = new CountryEntity(res.getString(R.string.Bosnia_and_Herzegovina), "+387");
        Botswana = new CountryEntity(res.getString(R.string.Botswana), "+267");
        Brazil = new CountryEntity(res.getString(R.string.Brazil), "+55");
        British_Virgin_Islands = new CountryEntity(res.getString(R.string.British_Virgin_Islands), "+1-284");
        Brunei_Darusalaam = new CountryEntity(res.getString(R.string.Brunei_Darusalaam), "+673");
        Bulgaria = new CountryEntity(res.getString(R.string.Bulgaria), "+359");
        Burkina_Faso = new CountryEntity(res.getString(R.string.Burkina_Faso), "+226");
        Burundi = new CountryEntity(res.getString(R.string.Burundi), "+257");
        Cambodia = new CountryEntity(res.getString(R.string.Cambodia), "+855");
        Cameroon = new CountryEntity(res.getString(R.string.Cameroon), "+237");
        Canada = new CountryEntity(res.getString(R.string.Canada), "+1");
        Cape_Verde = new CountryEntity(res.getString(R.string.Cape_Verde), "+238");
        Cayman_Islands = new CountryEntity(res.getString(R.string.Cayman_Islands), "+1-345");
        Central_African_Republic = new CountryEntity(res.getString(R.string.Central_African_Republic), "+236");
        Chad = new CountryEntity(res.getString(R.string.Chad), "+235");
        Chile = new CountryEntity(res.getString(R.string.Chile), "+56");
        China = new CountryEntity(res.getString(R.string.China), "+86");
        Christmas_Island = new CountryEntity(res.getString(R.string.Christmas_Island), "+672");
        Cocos_Islands = new CountryEntity(res.getString(R.string.Cocos_Islands), "+672");
        Colombia = new CountryEntity(res.getString(R.string.Colombia), "+57");
        Commonwealth_of_the_Northern_Mariana_Islands = new CountryEntity(res.getString(R.string.Commonwealth_of_the_Northern_Mariana_Islands), "+1-670");
        Comoros_and_Mayotte_Island = new CountryEntity(res.getString(R.string.Comoros_and_Mayotte_Island), "+269");
        Congo = new CountryEntity(res.getString(R.string.Congo), "+242");
        Cook_Islands = new CountryEntity(res.getString(R.string.Cook_Islands), "+682");
        Costa_Rica = new CountryEntity(res.getString(R.string.Costa_Rica), "+506");
        Croatia = new CountryEntity(res.getString(R.string.Croatia), "+385");
        Cuba = new CountryEntity(res.getString(R.string.Cuba), "+53");
        Cyprus = new CountryEntity(res.getString(R.string.Cyprus), "+357");
        Czech_Republic = new CountryEntity(res.getString(R.string.Czech_Republic), "+420");
        Denmark = new CountryEntity(res.getString(R.string.Denmark), "+45");
        Diego_Garcia = new CountryEntity(res.getString(R.string.Diego_Garcia), "+246");
        Djibouti = new CountryEntity(res.getString(R.string.Djibouti), "+253");
        Dominica = new CountryEntity(res.getString(R.string.Dominica), "+1-767");
        Dominican_Republic = new CountryEntity(res.getString(R.string.Dominican_Republic), "+1-809");
        East_Timor = new CountryEntity(res.getString(R.string.East_Timor), "+62");
        Ecuador = new CountryEntity(res.getString(R.string.Ecuador), "+593");
        Egypt = new CountryEntity(res.getString(R.string.Egypt), "+20");
        El_Salvador = new CountryEntity(res.getString(R.string.El_Salvador), "+503");
        Equatorial_Guinea = new CountryEntity(res.getString(R.string.Equatorial_Guinea), "+240");
        Estonia = new CountryEntity(res.getString(R.string.Estonia), "+372");
        Ethiopia = new CountryEntity(res.getString(R.string.Ethiopia), "+251");
        Faeroe_Islands = new CountryEntity(res.getString(R.string.Faeroe_Islands), "+298");
        Falkland_Islands = new CountryEntity(res.getString(R.string.Falkland_Islands), "+500");
        Fiji = new CountryEntity(res.getString(R.string.Fiji), "+679");
        Finland = new CountryEntity(res.getString(R.string.Finland), "+358");
        France = new CountryEntity(res.getString(R.string.France), "+33");
        French_Antilles = new CountryEntity(res.getString(R.string.French_Antilles), "+590");
        French_Guiana = new CountryEntity(res.getString(R.string.French_Guiana), "+594");
        French_Polynesia = new CountryEntity(res.getString(R.string.French_Polynesia), "+689");
        Gabon = new CountryEntity(res.getString(R.string.Gabon), "+241");
        Gambia = new CountryEntity(res.getString(R.string.Gambia), "+220");
        Georgia = new CountryEntity(res.getString(R.string.Georgia), "+995");
        Germany = new CountryEntity(res.getString(R.string.Germany), "+49");
        Ghana = new CountryEntity(res.getString(R.string.Ghana), "+233");
        Gibraltar = new CountryEntity(res.getString(R.string.Gibraltar), "+350");
        Greece = new CountryEntity(res.getString(R.string.Greece), "+30");
        Greenland = new CountryEntity(res.getString(R.string.Greenland), "+299");
        Grenada = new CountryEntity(res.getString(R.string.Grenada), "+1-473");
        Guam = new CountryEntity(res.getString(R.string.Guam), "+1-671, +671");
        Guatemala = new CountryEntity(res.getString(R.string.Guatemala), "+502");
        Guinea = new CountryEntity(res.getString(R.string.Guinea), "+224");
        Guinea_Bissau = new CountryEntity(res.getString(R.string.Guinea_Bissau), "+245");
        Guyana = new CountryEntity(res.getString(R.string.Guyana), "+592");
        Haiti = new CountryEntity(res.getString(R.string.Haiti), "+509");
        Honduras = new CountryEntity(res.getString(R.string.Honduras), "+504");
        Hong_Kong = new CountryEntity(res.getString(R.string.Hong_Kong), "+852");
        Hungary = new CountryEntity(res.getString(R.string.Hungary), "+36");
        Iceland = new CountryEntity(res.getString(R.string.Iceland), "+354");
        India = new CountryEntity(res.getString(R.string.India), "+91");
        Indonesia = new CountryEntity(res.getString(R.string.Indonesia), "+62");
        Iran = new CountryEntity(res.getString(R.string.Iran), "+98");
        Iraq = new CountryEntity(res.getString(R.string.Iraq), "+964");
        Irish_Republic = new CountryEntity(res.getString(R.string.Irish_Republic), "+353");
        Israel = new CountryEntity(res.getString(R.string.Israel), "+972");
        Italy = new CountryEntity(res.getString(R.string.Italy), "+39");
        Ivory_Coast = new CountryEntity(res.getString(R.string.Ivory_Coast), "+225");
        Jamaica = new CountryEntity(res.getString(R.string.Jamaica), "+1-876");
        Japan = new CountryEntity(res.getString(R.string.Japan), "+81");
        Jordan = new CountryEntity(res.getString(R.string.Jordan), "+962");
        Kazakhstan = new CountryEntity(res.getString(R.string.Kazakhstan), "+7");
        Kenya = new CountryEntity(res.getString(R.string.Kenya), "+254");
        Kiribati_Republic = new CountryEntity(res.getString(R.string.Kiribati_Republic), "+686");
        Kirgizia = new CountryEntity(res.getString(R.string.Kirgizia), "+996");
        Kuwait = new CountryEntity(res.getString(R.string.Kuwait), "+965");
        Laos = new CountryEntity(res.getString(R.string.Laos), "+856");
        Latvia = new CountryEntity(res.getString(R.string.Latvia), "+371");
        Lebanon = new CountryEntity(res.getString(R.string.Lebanon), "+961");
        Lesotho = new CountryEntity(res.getString(R.string.Lesotho), "+266");
        Liberia = new CountryEntity(res.getString(R.string.Liberia), "+231");
        Libya = new CountryEntity(res.getString(R.string.Libya), "+21");
        Liechtenstein = new CountryEntity(res.getString(R.string.Liechtenstein), "+41");
        Lithuania = new CountryEntity(res.getString(R.string.Lithuania), "+370");
        Luxembourg = new CountryEntity(res.getString(R.string.Luxembourg), "+352");
        Macao = new CountryEntity(res.getString(R.string.Macao), "+853");
        Macedonia = new CountryEntity(res.getString(R.string.Macedonia), "+389");
        Madagascar = new CountryEntity(res.getString(R.string.Madagascar), "+261");
        Malawi = new CountryEntity(res.getString(R.string.Malawi), "+265");
        Malaysia = new CountryEntity(res.getString(R.string.Malaysia), "+60");
        Maldives = new CountryEntity(res.getString(R.string.Maldives), "+960");
        Mali = new CountryEntity(res.getString(R.string.Mali), "+223");
        Malta = new CountryEntity(res.getString(R.string.Malta), "+356");
        Marshall_Islands = new CountryEntity(res.getString(R.string.Marshall_Islands), "+692");
        Martinique = new CountryEntity(res.getString(R.string.Martinique), "+596");
        Mauritania = new CountryEntity(res.getString(R.string.Mauritania), "+222");
        Mauritius = new CountryEntity(res.getString(R.string.Mauritius), "+230");
        Mexico = new CountryEntity(res.getString(R.string.Mexico), "+52");
        Micronesia = new CountryEntity(res.getString(R.string.Micronesia), "+691");
        Monaco = new CountryEntity(res.getString(R.string.Monaco), "+377");
        Mongolia = new CountryEntity(res.getString(R.string.Mongolia), "+976");
        Montserrat = new CountryEntity(res.getString(R.string.Montserrat), "+1-664");
        Morocco = new CountryEntity(res.getString(R.string.Morocco), "+212");
        Mozambique = new CountryEntity(res.getString(R.string.Mozambique), "+258");
        Myanmar = new CountryEntity(res.getString(R.string.Myanmar), "+95");
        Namibia = new CountryEntity(res.getString(R.string.Namibia), "+264");
        Nauru = new CountryEntity(res.getString(R.string.Nauru), "+674");
        Nepal = new CountryEntity(res.getString(R.string.Nepal), "+977");
        Netherlands = new CountryEntity(res.getString(R.string.Netherlands), "+31");
        Netherlands_Antilles = new CountryEntity(res.getString(R.string.Netherlands_Antilles), "+599");
        New_Caledonia = new CountryEntity(res.getString(R.string.New_Caledonia), "+687");
        New_Zealand = new CountryEntity(res.getString(R.string.New_Zealand), "+64");
        Nicaragua = new CountryEntity(res.getString(R.string.Nicaragua), "+505");
        Niger = new CountryEntity(res.getString(R.string.Niger), "+227");
        Nigeria = new CountryEntity(res.getString(R.string.Nigeria), "+234");
        Niue = new CountryEntity(res.getString(R.string.Niue), "+683");
        Norfolk_Island = new CountryEntity(res.getString(R.string.Norfolk_Island), "+672");
        North_Korea = new CountryEntity(res.getString(R.string.North_Korea), "+850");
        North_Yemen = new CountryEntity(res.getString(R.string.North_Yemen), "+967");
        Northern_Mariana_Islands = new CountryEntity(res.getString(R.string.Northern_Mariana_Islands), "+670");
        Norway = new CountryEntity(res.getString(R.string.Norway), "+47");
        Oman = new CountryEntity(res.getString(R.string.Oman), "+968");
        Pakistan = new CountryEntity(res.getString(R.string.Pakistan), "+92");
        Panama = new CountryEntity(res.getString(R.string.Panama), "+507");
        Papua_New_Guinea = new CountryEntity(res.getString(R.string.Papua_New_Guinea), "+675");
        Paraguay = new CountryEntity(res.getString(R.string.Paraguay), "+595");
        Peru = new CountryEntity(res.getString(R.string.Peru), "+51");
        Philippines = new CountryEntity(res.getString(R.string.Philippines), "+63");
        Poland = new CountryEntity(res.getString(R.string.Poland), "+48");
        Portugal = new CountryEntity(res.getString(R.string.Portugal), "+351");
        Puerto_Rico = new CountryEntity(res.getString(R.string.Puerto_Rico), "+1-787");
        Qatar = new CountryEntity(res.getString(R.string.Qatar), "+974");
        Republic_of_San_Marino = new CountryEntity(res.getString(R.string.Republic_of_San_Marino), "+378");
        Reunion = new CountryEntity(res.getString(R.string.Reunion), "+262");
        Romania = new CountryEntity(res.getString(R.string.Romania), "+40");
        Russia = new CountryEntity(res.getString(R.string.Russia), "+7");
        Rwandese_Republic = new CountryEntity(res.getString(R.string.Rwandese_Republic), "+250");
        Saint_Helena_and_Ascension_Island = new CountryEntity(res.getString(R.string.Saint_Helena_and_Ascension_Island), "+247");
        Saint_Pierre_et_Miquelon = new CountryEntity(res.getString(R.string.Saint_Pierre_et_Miquelon), "+508");
        San_Marino = new CountryEntity(res.getString(R.string.San_Marino), "+39");
        Sao_Tome_e_Principe = new CountryEntity(res.getString(R.string.Sao_Tome_e_Principe), "+239");
        Saudi_Arabia = new CountryEntity(res.getString(R.string.Saudi_Arabia), "+966");
        Senegal = new CountryEntity(res.getString(R.string.Senegal), "+221");
        Seychelles = new CountryEntity(res.getString(R.string.Seychelles), "+248");
        Sierra_Leone = new CountryEntity(res.getString(R.string.Sierra_Leone), "+232");
        Singapore = new CountryEntity(res.getString(R.string.Singapore), "+65");
        Slovakia = new CountryEntity(res.getString(R.string.Slovakia), "+421");
        Slovenia = new CountryEntity(res.getString(R.string.Slovenia), "+386");
        Solomon_Islands = new CountryEntity(res.getString(R.string.Solomon_Islands), "+677");
        Somalia = new CountryEntity(res.getString(R.string.Somalia), "+252");
        South_Africa = new CountryEntity(res.getString(R.string.South_Africa), "+27");
        South_Korea = new CountryEntity(res.getString(R.string.South_Korea), "+82");
        South_Yemen = new CountryEntity(res.getString(R.string.South_Yemen), "+969");
        Spain = new CountryEntity(res.getString(R.string.Spain), "+34");
        Sri_Lanka = new CountryEntity(res.getString(R.string.Sri_Lanka), "+94");
        St_Kitts_and_Nevis = new CountryEntity(res.getString(R.string.St_Kitts_and_Nevis), "+1-869");
        St_Lucia = new CountryEntity(res.getString(R.string.St_Lucia), "+1-758");
        St_Vincent_and_the_Grenadines = new CountryEntity(res.getString(R.string.St_Vincent_and_the_Grenadines), "+1-784");
        Sudan = new CountryEntity(res.getString(R.string.Sudan), "+249");
        Suriname = new CountryEntity(res.getString(R.string.Suriname), "+597");
        Svalbard_and_Jan_Mayen_Islands = new CountryEntity(res.getString(R.string.Svalbard_and_Jan_Mayen_Islands), "+47");
        Swaziland = new CountryEntity(res.getString(R.string.Swaziland), "+268");
        Sweden = new CountryEntity(res.getString(R.string.Sweden), "+46");
        Switzerland = new CountryEntity(res.getString(R.string.Switzerland), "+41");
        Syria = new CountryEntity(res.getString(R.string.Syria), "+963");
        Tajikistan = new CountryEntity(res.getString(R.string.Tajikistan), "+992");
        Taiwan = new CountryEntity(res.getString(R.string.Taiwan), "+886");
        Tanzania = new CountryEntity(res.getString(R.string.Tanzania), "+255");
        Thailand = new CountryEntity(res.getString(R.string.Thailand), "+66");
        Togolese_Republic = new CountryEntity(res.getString(R.string.Togolese_Republic), "+228");
        Tokelau = new CountryEntity(res.getString(R.string.Tokelau), "+690");
        Tonga = new CountryEntity(res.getString(R.string.Tonga), "+676");
        Trinidad_and_Tobago = new CountryEntity(res.getString(R.string.Trinidad_and_Tobago), "+1-868");
        Tunisia = new CountryEntity(res.getString(R.string.Tunisia), "+21");
        Turkey = new CountryEntity(res.getString(R.string.Turkey), "+90");
        Turkmenistan = new CountryEntity(res.getString(R.string.Turkmenistan), "+993");
        Turks_And_Caicos_Islands = new CountryEntity(res.getString(R.string.Turks_And_Caicos_Islands), "+1-649");
        Tuvalu = new CountryEntity(res.getString(R.string.Tuvalu), "+688");
        US_Virgin_Islands = new CountryEntity(res.getString(R.string.US_Virgin_Islands), "+1-340");
        Uganda = new CountryEntity(res.getString(R.string.Uganda), "+256");
        Ukraine = new CountryEntity(res.getString(R.string.Ukraine), "+380");
        United_Arab_Emirates = new CountryEntity(res.getString(R.string.United_Arab_Emirates), "+971");
        United_Kingdom = new CountryEntity(res.getString(R.string.United_Kingdom), "+44");
        Uruguay = new CountryEntity(res.getString(R.string.Uruguay), "+598");
        USA = new CountryEntity(res.getString(R.string.USA), "+1");
        Uzbekistan = new CountryEntity(res.getString(R.string.Uzbekistan), "+998");
        Vanuatu = new CountryEntity(res.getString(R.string.Vanuatu), "+678");
        Vatican_City = new CountryEntity(res.getString(R.string.Vatican_City), "+39");
        Venezuela = new CountryEntity(res.getString(R.string.Venezuela), "+58");
        Vietnam = new CountryEntity(res.getString(R.string.Vietnam), "+84");
        Wales = new CountryEntity(res.getString(R.string.Wallis_and_Futuna_Islands), "+681");
        Western_Sahara = new CountryEntity(res.getString(R.string.Western_Sahara), "+21");
        Western_Samoa = new CountryEntity(res.getString(R.string.Western_Samoa), "+685");
        Yugoslavia = new CountryEntity(res.getString(R.string.Yugoslavia), "+381");
        Zaire = new CountryEntity(res.getString(R.string.Zaire), "+243");
        Zambia = new CountryEntity(res.getString(R.string.Zambia), "+260");
        Zimbabwe = new CountryEntity(res.getString(R.string.Zimbabwe), "+263");
        all_countries = new ArrayList<CountryEntity>(Arrays.asList(Afghanistan, Albania, Algeria, Andorra, Angola, Anguilla, Armenia, Argentina, Australia, Austria, Azerbaijan, Bahamas, Bahrain, Bangladesh, Barbados, Belarus, Belgium, Belize, Benin, Bolivia, Bosnia_and_Herzegovina, Botswana, Brazil, Bulgaria, Cambodia, Cameroon, Canada, Cape_Verde, Chad, Chile, China, Colombia, Congo, Costa_Rica, Croatia, Cuba, Cyprus, Czech_Republic, Denmark, Djibouti, Dominica, Dominican_Republic, East_Timor, Ecuador, Egypt, El_Salvador, Estonia, Ethiopia, Faeroe_Islands, Falkland_Islands, Fiji, Finland, France, Gabon, Gambia, Georgia, Germany, Ghana, Gibraltar, Greece, Greenland, Grenada, Guatemala, Guinea, Guyana, Haiti, Honduras, Hong_Kong, Hungary, Iceland, India, Indonesia, Iran, Iraq, Irish_Republic, Israel, Italy, Jamaica, Japan, Jordan, Kazakhstan, Kenya, Kirgizia, Kuwait, Laos, Latvia, Lebanon, Liberia, Libya, Liechtenstein, Lithuania, Luxembourg, Macao, Macedonia, Madagascar, Malaysia, Maldives, Mali, Malta, Mauritania, Mauritius, Mexico, Monaco, Mongolia, Morocco, Myanmar, Namibia, Nepal, Netherlands, New_Zealand, Nicaragua, Niger, Nigeria, Norfolk_Island, North_Korea, Norway, Oman, Pakistan, Panama, Paraguay, Peru, Philippines, Poland, Portugal, Puerto_Rico, Qatar, Republic_of_San_Marino, Romania, Russia, San_Marino, Saudi_Arabia, Senegal, Seychelles, Sierra_Leone, Singapore, Slovakia, Slovenia, Somalia, South_Africa, South_Korea, Spain, Sri_Lanka, Sudan, Sweden, Switzerland, Syria, Tajikistan, Taiwan, Thailand, Tonga, Trinidad_and_Tobago, Tunisia, Turkey, Turkmenistan, Uganda, Ukraine, United_Arab_Emirates, United_Kingdom, Uruguay, USA, Uzbekistan, Vatican_City, Venezuela, Vietnam, Zaire, Zambia, Zimbabwe));
        Collections.sort(all_countries, (o1, o2) -> {
            try {
                return o1.getName().compareTo(o2.getName());
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        });
    }


    public void print_fields() {
        MainActivity.logstr("PRINT_FIELDS " + all_countries.size());
        for (int i = 0; i < all_countries.size(); i++) {
            CountryEntity c = all_countries.get(i);
            MainActivity.logstr(c.name + " " + c.code);

        }

    }

    public String getId(String name) throws Exception {
        Field[] fields = this.getClass().getFields();
        for (int i = 0; i < fields.length; i++) {
            CountryEntity co = (CountryEntity) fields[i].get(this);
            if (co != null) if (co.getName().equals(name)) return fields[i].getName();
        }
        return "false";
    }

    public PagedList<CountryEntity> getCountriesList(String name) {


        List<CountryEntity> search_data = new ArrayList<>();
        for (int i = 0; i < all_countries.size(); i++) {
            if (all_countries.get(i).name.toLowerCase().indexOf(name) != -1) {
                MainActivity.logstr(all_countries.get(i).name + " " + name);
                search_data.add(all_countries.get(i));
            }
        }
        PagedList.Config myConfig = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(search_data.size())
                .setPageSize(1)
                .build();
        List<CountryEntity> myList = search_data;
        StringListProvider provider = new StringListProvider(myList);
        StringDataSource dataSource = new StringDataSource(provider);
        PagedList<CountryEntity> pagedStrings = new PagedList.Builder<Integer, CountryEntity>(new ListDataSource(myList), myConfig)
                .setInitialKey(0)
                .setNotifyExecutor(new UiThreadExecutor())
                .setFetchExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                .build();

        MainActivity.logstr("SAVE_COUNTRIES " + all_countries.size());

        MainActivity.logstr("SAVE_LIVE_COUNTRIES ");
        return pagedStrings;
    }

    class ListDataSource extends PositionalDataSource<CountryEntity> {
        private List<CountryEntity> items;

        public ListDataSource(List<CountryEntity> list) {
            this.items = list;
        }

        @Override
        public void loadInitial(LoadInitialParams params, LoadInitialCallback<CountryEntity> callback) {
            callback.onResult(items, 0, items.size());
        }

        @Override
        public void loadRange(LoadRangeParams params, LoadRangeCallback<CountryEntity> callback) {
            int start = params.startPosition;
            int end = params.startPosition + params.loadSize;
            callback.onResult(items.subList(start, end));
        }
    }

    class UiThreadExecutor implements Executor {
        private Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable runnable) {
            handler.post(runnable);
        }

    }

    public static class StringListProvider {

        private List<CountryEntity> list;

        public StringListProvider(List<CountryEntity> list) {
            this.list = list;
        }

        public List<CountryEntity> getStringList(int page, int pageSize) {
            int initialIndex = page * pageSize;
            int finalIndex = initialIndex + pageSize;


            return list.subList(initialIndex, finalIndex);
        }
    }

    public static class StringDataSource extends PageKeyedDataSource<Integer, CountryEntity> {

        public static final int PAGE_SIZE = 20;
        private StringListProvider provider;

        public StringDataSource(StringListProvider provider) {
            this.provider = provider;
        }

        @Override
        public void loadInitial(@NonNull PageKeyedDataSource.LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, CountryEntity> callback) {
            List<CountryEntity> result = provider.getStringList(0, params.requestedLoadSize);
            callback.onResult(result, 1, 2);
        }

        @Override
        public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, CountryEntity> callback) {
            List<CountryEntity> result = provider.getStringList(params.key, params.requestedLoadSize);
            Integer nextIndex = null;

            if (params.key > 1) {
                nextIndex = params.key - 1;
            }
            callback.onResult(result, nextIndex);
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, CountryEntity> callback) {
            List<CountryEntity> result = provider.getStringList(params.key, params.requestedLoadSize);
            callback.onResult(result, params.key + 1);
        }
    }


}
