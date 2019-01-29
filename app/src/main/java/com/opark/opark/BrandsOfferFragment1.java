package com.opark.opark;

        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.opark.opark.merchant_side.merchant_offer.MerchantOffer;
        import com.opark.opark.merchant_side.merchant_offer.MerchantOfferAdapter;
        import com.opark.opark.rewards_redemption.ConfirmPreRedeem;
        import com.opark.opark.rewards_redemption.RewardsFragment;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;

public class BrandsOfferFragment1 extends Fragment implements BrandsTitleAdapter.BrandsSelected {

    final List<MerchantOffer> unityBrandOffer = new ArrayList<>();
    final List<MerchantOffer> merchantOffer = new ArrayList<>();

    public static String unityName;

    public static BrandsTitleAdapter brandsTitleAdapter;
    public static MerchantOfferAdapter merchantOfferAdapter;
    public static ConfirmPreRedeem confirmPreRedeem ;
    public static ShowBrandOffer showBrandOffer1;

    private TextView aText ;
    private LinearLayout linearLayout;


    public static List<BrandsName> brandsNamesListA = new ArrayList<>();
    public static List<String> brandsNameListStringA = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferA  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListB = new ArrayList<>();
    public static List<String> brandsNameListStringB = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferB  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListC = new ArrayList<>();
    public static List<String> brandsNameListStringC = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferC  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListD = new ArrayList<>();
    public static List<String> brandsNameListStringD = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferD  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListE = new ArrayList<>();
    public static List<String> brandsNameListStringE = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferE  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListF = new ArrayList<>();
    public static List<String> brandsNameListStringF = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferF  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListG = new ArrayList<>();
    public static List<String> brandsNameListStringG = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferG  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListH = new ArrayList<>();
    public static List<String> brandsNameListStringH = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferH  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListI = new ArrayList<>();
    public static List<String> brandsNameListStringI = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferI  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListJ = new ArrayList<>();
    public static List<String> brandsNameListStringJ = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferJ  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListK = new ArrayList<>();
    public static List<String> brandsNameListStringK = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferK  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListL = new ArrayList<>();
    public static List<String> brandsNameListStringL = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferL  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListM = new ArrayList<>();
    public static List<String> brandsNameListStringM = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferM  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListN = new ArrayList<>();
    public static List<String> brandsNameListStringN = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferN  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListO = new ArrayList<>();
    public static List<String> brandsNameListStringO = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferO  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListP = new ArrayList<>();
    public static List<String> brandsNameListStringP = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferP  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListQ = new ArrayList<>();
    public static List<String> brandsNameListStringQ = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferQ  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListR = new ArrayList<>();
    public static List<String> brandsNameListStringR = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferR  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListS = new ArrayList<>();
    public static List<String> brandsNameListStringS = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferS  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListT = new ArrayList<>();
    public static List<String> brandsNameListStringT = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferT  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListU = new ArrayList<>();
    public static List<String> brandsNameListStringU = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferU  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListV = new ArrayList<>();
    public static List<String> brandsNameListStringV = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferV  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListW = new ArrayList<>();
    public static List<String> brandsNameListStringW = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferW  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListX = new ArrayList<>();
    public static List<String> brandsNameListStringX = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferX  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListY = new ArrayList<>();
    public static List<String> brandsNameListStringY = new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferY  = new ArrayList<>();
    public static List<BrandsName> brandsNamesListZ = new ArrayList<>();
    public static List<String> brandsNameListStringZ= new ArrayList<>();
    public static final List<MerchantOffer> brandsOfferZ  = new ArrayList<>();




    private RecyclerView recViewA;
    private RecyclerView recViewB;
    private RecyclerView recViewC;
    private RecyclerView recViewD;
    private RecyclerView recViewE;
    private RecyclerView recViewF;
    private RecyclerView recViewG;
    private RecyclerView recViewH;
    private RecyclerView recViewI;
    private RecyclerView recViewJ;
    private RecyclerView recViewK;
    private RecyclerView recViewL;
    private RecyclerView recViewM;
    private RecyclerView recViewN;
    private RecyclerView recViewO;
    private RecyclerView recViewP;
    private RecyclerView recViewQ;
    private RecyclerView recViewR;
    private RecyclerView recViewS;
    private RecyclerView recViewT;
    private RecyclerView recViewU;
    private RecyclerView recViewV;
    private RecyclerView recViewW;
    private RecyclerView recViewX;
    private RecyclerView recViewY;
    private RecyclerView recViewZ;
    public static  FragmentManager fm;
    public static FragmentTransaction ft;
//    public static   List<BrandsName> brandsNameListK = new ArrayList<>();
    public static  List <MerchantOffer> brandsOfferList = new ArrayList<>();
    private  List<String> brandsNameinString = new ArrayList<>();
    private  BrandsAlphabet brandsAlphabet;
    private DatabaseReference brandsTitleAlphabetDatabase;
    private DatabaseReference offerlistDatabaseRef;
    public static BrandsOfferTItleAlphabetAdapter brandsOfferTItleAlphabetAdapter;

    public static String brandsNameForReplaceFragment;


    public updatePage updatePage;



    public interface updatePage {
        void updatePage();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.brands_title_view1,container,false);


        bindViews(view);
        offerlistDatabaseRef = FirebaseDatabase.getInstance().getReference().child("offerlist");

        brandsTitleAlphabetDatabase = FirebaseDatabase.getInstance().getReference().child("offerlist/");

        fm = getChildFragmentManager();
         ft = fm.beginTransaction();
//
//        if (fm != null) {
//            Log.d("fm", " not null ");
//            FragmentTransaction transaction = fm.beginTransaction();
//
//            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//
//            transaction.replace(R.id.brands_title_replaceable_layout, new ShowBrandOffer());
//            transaction.addToBackStack("test");
//            transaction.commit();
//
//
//            fm.beginTransaction().replace(R.id.brands_title_replaceable_layout, new ShowBrandOffer()).commitNow();
//        }


//        transaction = getParentFragment().getFragmentManager().beginTransaction();
//        transaction.replace(R.id.brands_title_replaceable_layout, new ShowBrandOffer());
//        transaction.commit();



//        aText.getLocationOnScreen();
//
//        brandsAlphabetRecView.setHasFixedSize(true);
//        brandsAlphabetRecView.setLayoutManager(llm);
        initializeData();


        return view;
    }






//    private void initializeData1(){
//      merchantOffer.addAll(AllOfferFragment.merchantOfferForBrands);
//
//
//
//        Log.d("brands", "initializeData1: " + merchantOffer);
//
//        brandsTitleAdapter = new BrandsTitleAdapter(brandsNameListK, new BrandsTitleAdapter.BrandsSelected() {
//            @Override
//            public void onBrandsSelected(View v, int position) {
//
//            }
//        });
//
//        LinearLayoutManager llm = new LinearLayoutManager(getContext());
//        recViewK.setLayoutManager(llm);
//        recViewK.setAdapter(brandsTitleAdapter);
//
//    }


    private void initializeData(){

        Log.d("INITDATA", "initializeData: initialising data");


//        merchantOffer = new ArrayList<>();
        offerlistDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("tab", "onChildAdded: datasnapshot key" + dataSnapshot.getKey());
                Log.d("tab", "onChildAdded: datasnapshot children" + dataSnapshot.getChildren());
                Log.d("tab", "onChildAdded: datasnapshot " +  String.valueOf(dataSnapshot.child("offerCost").getValue()));
                Log.d("tab", "onChildAdded:  datasnapshot value " + dataSnapshot.getValue());
                Log.d("tab", "onChildAdded: not adding " + dataSnapshot.hasChild("merchantsName"));




//                merchantOffer.add(new MerchantOffer(String.valueOf(dataSnapshot.child("merchantOfferTitle").getValue()) ,String.valueOf(dataSnapshot.child("merchantName").getValue()),String.valueOf(dataSnapshot.child("merchantAddress").getValue()),String.valueOf(dataSnapshot.child("merchantContact").getValue()),String.valueOf(dataSnapshot.child("offerCost").getValue())));

                if (!dataSnapshot.getKey().equals("merchantsName") ) {
                    merchantOffer.add(dataSnapshot.getValue(MerchantOffer.class));


                    Character alphabet = dataSnapshot.getValue(MerchantOffer.class).getMerchantName().charAt(0);
                    switch (alphabet){

                        case 'A':
                            addMerchantsBrandName1(alphabet,dataSnapshot,recViewA,brandsNamesListA,brandsNameListStringA,brandsOfferA);

                            break;
                        case 'B':
                            addMerchantsBrandName1(alphabet,dataSnapshot,recViewB,brandsNamesListB,brandsNameListStringB,brandsOfferB);

                            break;
                        case 'C':
                            addMerchantsBrandName(alphabet,dataSnapshot,recViewC);

                            break;
                        case 'D':
                            addMerchantsBrandName(alphabet,dataSnapshot,recViewD);

                            break;
                        case 'E':
                            addMerchantsBrandName(alphabet,dataSnapshot,recViewE);

                            break;
                        case 'F':
                            addMerchantsBrandName(alphabet,dataSnapshot,recViewF);

                            break;
                        case 'G':
                            addMerchantsBrandName(alphabet,dataSnapshot,recViewG);

                            break;
                        case 'H':
                            addMerchantsBrandName(alphabet,dataSnapshot,recViewH);

                            break;
                        case 'I':
                            addMerchantsBrandName(alphabet,dataSnapshot,recViewI);

                            break;
                        case 'J':
                            addMerchantsBrandName(alphabet,dataSnapshot,recViewJ);

                            break;

                        case 'K':

                            addMerchantsBrandName1(alphabet,dataSnapshot,recViewK,brandsNamesListK,brandsNameListStringK,brandsOfferK);


                                break;

                        case 'L':
                            addMerchantsBrandName1(alphabet,dataSnapshot,recViewL,brandsNamesListL,brandsNameListStringL,brandsOfferL);

                            break;

                        case 'M':
                            addMerchantsBrandName1(alphabet,dataSnapshot,recViewM,brandsNamesListM,brandsNameListStringM,brandsOfferM);

                            break;

                        case 'm':
                            addMerchantsBrandName1(alphabet,dataSnapshot,recViewM,brandsNamesListM,brandsNameListStringM,brandsOfferM);

                            break;
                        case 'N':


                            Log.d("brands", "in m: " + alphabet);
//                            Log.d("brands", "in m: " + dataSnapshot.getValue(MerchantOffer.class).getMerchantName().charAt(0));


                            addMerchantsBrandName(alphabet,dataSnapshot,recViewN);


                            break;



                        case 'O':
                            addMerchantsBrandName(alphabet,dataSnapshot,recViewO);

                            break;
                        case 'P':
                            addMerchantsBrandName(alphabet,dataSnapshot,recViewP);

                            break;
                        case 'Q':
                            addMerchantsBrandName(alphabet,dataSnapshot,recViewQ);

                            break;
                        case 'R':
                            addMerchantsBrandName(alphabet,dataSnapshot,recViewR);

                            break;
                        case 'S':
                            addMerchantsBrandName(alphabet,dataSnapshot,recViewS);

                            break;
                        case 'T':
                            addMerchantsBrandName(alphabet,dataSnapshot,recViewT);

                            break;
                        case 'U':
                            addMerchantsBrandName(alphabet,dataSnapshot,recViewU);

                            break;
                        case 'V':
                            addMerchantsBrandName(alphabet,dataSnapshot,recViewV);

                            break;
                        case 'W':
                            addMerchantsBrandName(alphabet,dataSnapshot,recViewW);

                            break;
                        case 'X':
                            addMerchantsBrandName(alphabet,dataSnapshot,recViewX);

                            break;
                        case 'Y':
                            addMerchantsBrandName(alphabet,dataSnapshot,recViewY);

                            break;
                        case 'Z':
                            addMerchantsBrandName(alphabet,dataSnapshot,recViewZ);

                            break;





                        default:

                    }



                }

            }

//           String.valueOf(dataSnapshot.child("offerImage").getValue())

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
















    private void  bindViews(View view){

        aText = view.findViewById(R.id.brands_a);
        linearLayout = view.findViewById(R.id.brands_title_replaceable_layout);

        recViewA= view.findViewById(R.id.brands_a_recview);
        recViewB= view.findViewById(R.id.brands_b_recview);
        recViewC= view.findViewById(R.id.brands_c_recview);
        recViewD= view.findViewById(R.id.brands_d_recview);
        recViewE= view.findViewById(R.id.brands_e_recview);
        recViewF= view.findViewById(R.id.brands_f_recview);
        recViewG= view.findViewById(R.id.brands_g_recview);
        recViewH= view.findViewById(R.id.brands_h_recview);
        recViewI= view.findViewById(R.id.brands_i_recview);
        recViewJ= view.findViewById(R.id.brands_j_recview);
        recViewK= view.findViewById(R.id.brands_k_recview);
        recViewL= view.findViewById(R.id.brands_l_recview);
        recViewM= view.findViewById(R.id.brands_m_recview);
        recViewN= view.findViewById(R.id.brands_n_recview);
        recViewO= view.findViewById(R.id.brands_o_recview);
        recViewP= view.findViewById(R.id.brands_p_recview);
        recViewQ= view.findViewById(R.id.brands_q_recview);
        recViewR= view.findViewById(R.id.brands_r_recview);
        recViewS= view.findViewById(R.id.brands_s_recview);
        recViewT= view.findViewById(R.id.brands_t_recview);
        recViewU= view.findViewById(R.id.brands_u_recview);
        recViewV= view.findViewById(R.id.brands_v_recview);
        recViewW= view.findViewById(R.id.brands_w_recview);
        recViewX= view.findViewById(R.id.brands_x_recview);
        recViewY= view.findViewById(R.id.brands_y_recview);
        recViewZ= view.findViewById(R.id.brands_z_recview);



    }

    @Override
    public void onBrandsSelected(View v, int position) {

        Log.d("selected", "onBrandsSelected: ");
//        transaction.replace(R.id.brands_title_replaceable_layout, new ShowBrandOffer());
//        transaction.commit();



    }





    private void addMerchantsBrandName(Character alphabet,DataSnapshot dataSnapshot, RecyclerView recview){





        List<BrandsName> brandsNamesList = new ArrayList<>();
        List<String> brandsNameListString = new ArrayList<>();
        final List<MerchantOffer> brandsOffer  = new ArrayList<>();



        brandsOffer.add(dataSnapshot.getValue(MerchantOffer.class));


        Log.d("brands", "is " + alphabet + ":  array is " + brandsNamesList);
        BrandsName brandsname = new BrandsName();
        brandsname.setBrandsName(dataSnapshot.getValue(MerchantOffer.class).getMerchantName());
        Character  aaaa = dataSnapshot.getValue(MerchantOffer.class).getMerchantName().charAt(0);

        Log.d("brands", "addMerchantsBrandName:  alphabet is "+alphabet +" and is correct " +  aaaa.equals(alphabet) +" Merchant Name is "+ dataSnapshot.getValue(MerchantOffer.class).getMerchantName() );

        Log.d("brands", "name list string: " + brandsNameListString + " brandsname List " + brandsNamesList);
        if (dataSnapshot.getValue(MerchantOffer.class).getMerchantName().charAt(0)==alphabet && !brandsNamesList.contains(brandsname)&& (!brandsNameListString.contains(brandsname.getBrandsName()))){


            brandsNamesList.add(brandsname);
            brandsNameListString.add(brandsname.getBrandsName());

            brandsTitleAdapter = new BrandsTitleAdapter(brandsNamesList, new BrandsTitleAdapter.BrandsSelected() {
                @Override
                public void onBrandsSelected(View v, int position) {
                    Log.d("selected", "inside:  " +  v);
//




                    ft.replace(R.id.lalala,new ShowBrandOffer());
                    ft.addToBackStack(null);
                    ft.commit();
                    brandsOfferList.addAll(brandsOffer);


                    Log.d("selected", "brandsoffer: " + brandsOffer.size() + " brandsOfferlist " +  brandsOfferList.size());


//
//
//
// View C = getLayoutInflater().inflate(R.id.brands_c_recview,v,false);
//                    RewardsFragment.mViewPager.addView(v);
                }
            });

            Log.d("brands", "brandsnamelist " + brandsNamesList);

            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            recview.setLayoutManager(llm);
            recview.setAdapter(brandsTitleAdapter);

//
//            transaction.replace(R.id.brands_title_replaceable_layout, new ShowBrandOffer());
//            transaction.commit();


        }
    }







    private void addMerchantsBrandName1(Character alphabet, DataSnapshot dataSnapshot, RecyclerView recview, final List<BrandsName> brandsNamesList, final List<String> brandsNameListString, final List<MerchantOffer> brandsOffer){




        brandsOffer.add(dataSnapshot.getValue(MerchantOffer.class));


        Log.d("brands", "is " + alphabet + ":  array is " + brandsNamesList);
        final BrandsName brandsname = new BrandsName();
        brandsname.setBrandsName(dataSnapshot.getValue(MerchantOffer.class).getMerchantName());
        Character  aaaa = dataSnapshot.getValue(MerchantOffer.class).getMerchantName().charAt(0);

        Log.d("brands", "addMerchantsBrandName:  alphabet is "+alphabet +" and is correct " +  aaaa.equals(alphabet) +" Merchant Name is "+ dataSnapshot.getValue(MerchantOffer.class).getMerchantName() );

        Log.d("brands", "name list string: " + brandsNameListString + " brandsname List " + brandsNamesList);
        if (dataSnapshot.getValue(MerchantOffer.class).getMerchantName().charAt(0)==alphabet && !brandsNamesList.contains(brandsname)&& (!brandsNameListString.contains(brandsname.getBrandsName()))){

            Log.d("brands", "alphabet correct, brandsnamelist doesnt contain brandsname, brandsnameliststring doent contain brandsname ");
            brandsNamesList.add(brandsname);
            brandsNameListString.add(brandsname.getBrandsName());

            brandsTitleAdapter = new BrandsTitleAdapter(brandsNamesList, new BrandsTitleAdapter.BrandsSelected() {
                @Override
                public void onBrandsSelected(View v, int position) {
                    Log.d("selected", "inside:  " +  v);
                    Log.d("selected", "selected brand is :  " + brandsname.getBrandsName());
//
//                    brandsOfferList.clear();

                    BrandsOfferFragment.brandsOfferFragMan.beginTransaction().replace(R.id.frame_container,BrandsOfferFragment.showBrandOffer,"showingbrand")
//                            .addToBackStack("showingBrand")
                            .commit();

//                    fm.beginTransaction().add(R.id.lalala,new ShowBrandOffer(), "showingbrand").commit();

//                    brandsOfferList.addAll(brandsOffer);


                    Log.d("selected", "brandsoffer: " + brandsOffer.size() + " brandsOfferlist " +  brandsOfferList.size());


                    brandsNameListString.clear();
                    brandsNamesList.clear();
//
//
//
// View C = getLayoutInflater().inflate(R.id.brands_c_recview,v,false);
//                    RewardsFragment.mViewPager.addView(v);
                }
            });

            Log.d("brands", "brandsnamelist " + brandsNamesList);

            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            recview.setLayoutManager(llm);
            recview.setAdapter(brandsTitleAdapter);

//
//            transaction.replace(R.id.brands_title_replaceable_layout, new ShowBrandOffer());
//            transaction.commit();


        }
    }







    private void addMerchantsBrandName2(Character alphabet,DataSnapshot dataSnapshot, RecyclerView recview, List<BrandsName> brandsNamesList, List<String> brandsNameListString, final List<MerchantOffer> brandsOffer){




        brandsOffer.add(dataSnapshot.getValue(MerchantOffer.class));


        Log.d("brands", "is " + alphabet + ":  array is " + brandsNamesList);
        final BrandsName brandsname = new BrandsName();
        brandsname.setBrandsName(dataSnapshot.getValue(MerchantOffer.class).getMerchantName());
        Character  aaaa = dataSnapshot.getValue(MerchantOffer.class).getMerchantName().charAt(0);

        Log.d("brands", "addMerchantsBrandName:  alphabet is "+alphabet +" and is correct " +  aaaa.equals(alphabet) +" Merchant Name is "+ dataSnapshot.getValue(MerchantOffer.class).getMerchantName() );

        Log.d("brands", "name list string: " + brandsNameListString + " brandsname List " + brandsNamesList);
        if (dataSnapshot.getValue(MerchantOffer.class).getMerchantName().charAt(0)==alphabet && !brandsNamesList.contains(brandsname)&& (!brandsNameListString.contains(brandsname.getBrandsName()))){


            brandsNamesList.add(brandsname);
            brandsNameListString.add(brandsname.getBrandsName());

            brandsTitleAdapter = new BrandsTitleAdapter(brandsNamesList, new BrandsTitleAdapter.BrandsSelected() {
                @Override
                public void onBrandsSelected(View v, int position) {
                    Log.d("selected", "inside:  " +  v);
                    Log.d("selected", "selected brand is :  " + brandsname.getBrandsName());
//
                    brandsOfferList.clear();
                    showBrandOffer1 = new ShowBrandOffer();

                    ft.replace(R.id.lalala,showBrandOffer1);
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);
                    ft.commit();
                    brandsOfferList.addAll(brandsOffer);


                    Log.d("selected", "brandsoffer: " + brandsOffer.size() + " brandsOfferlist " +  brandsOfferList.size());


//
//
//
// View C = getLayoutInflater().inflate(R.id.brands_c_recview,v,false);
//                    RewardsFragment.mViewPager.addView(v);
                }
            });

            Log.d("brands", "brandsnamelist " + brandsNamesList);

            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            recview.setLayoutManager(llm);
            recview.setAdapter(brandsTitleAdapter);

//
//            transaction.replace(R.id.brands_title_replaceable_layout, new ShowBrandOffer());
//            transaction.commit();


        }
    }


    @Override
    public void onDetach() {
        super.onDetach();

        ShowBrandOffer.clearArrayList();


    }
}





