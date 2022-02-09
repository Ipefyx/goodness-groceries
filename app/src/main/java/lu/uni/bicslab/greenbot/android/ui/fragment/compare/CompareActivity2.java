package lu.uni.bicslab.greenbot.android.ui.fragment.compare;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.datamodel.IndicatorCategoryModel;
import lu.uni.bicslab.greenbot.android.datamodel.IndicatorModel;
import lu.uni.bicslab.greenbot.android.datamodel.ProductCategoryModel;
import lu.uni.bicslab.greenbot.android.datamodel.ProductModel;
import lu.uni.bicslab.greenbot.android.other.CompareModel;
import lu.uni.bicslab.greenbot.android.other.Utils;

public class CompareActivity2 extends AppCompatActivity {

    private ProductModel comparedProduct;
    private Context mContext;
    private List<CompareModel> compareModels;

    // Pour chaque categorie d'indicateurs, une liste des indicateurs de cette cat
    List<IndicatorModel> indCatEnvironmentList = new ArrayList<>();
    List<IndicatorModel> indCatSocialList = new ArrayList<>();
    List<IndicatorModel> indCatGoodGovernanceList = new ArrayList<>();
    List<IndicatorModel> indCatEconomicList = new ArrayList<>();


    private TableLayout productsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare2);

        mContext = getApplicationContext();
        comparedProduct = (ProductModel) getIntent().getSerializableExtra("key_product");

        readData();
        displayData();
    }

    private void readData() {
        // Liste des categories des indicateurs existantes
        List<IndicatorCategoryModel> indicatorCategoryModels = Utils.getIndicatorCategoryList(mContext);
        // Liste des indicateurs existants dans la DB
        List<IndicatorModel> indicatorModels = Utils.getIndicatorList(mContext);
        // Liste de tous les produits dans la DB
        List<ProductModel> productModels = Utils.getProductList(mContext);

        // Liste des CompareModel contenenant les data permettant de comparer entre eux
        compareModels = new ArrayList<>();

        // Comparaison avec chaque produit existant
        for (ProductModel pm : productModels) {
            // Si type des 2 produits sont les même
            if(comparedProduct.getType().equals(pm.getType())) {
                // Ajout de cet item à la liste de comparaison

                // Creer pour chaque categorie d'indicateurs, une liste des indicateurs de cette cat
                indCatEnvironmentList = new ArrayList<>();
                indCatSocialList = new ArrayList<>();
                indCatGoodGovernanceList = new ArrayList<>();
                indCatEconomicList = new ArrayList<>();

                // Pour chaque indicateur existant
                for(IndicatorModel im : indicatorModels) {
                    // Verifier et selectionner si le produit contient cet indicateur
                    IndicatorModel nIm = new IndicatorModel(im);
                    nIm.setSelected(pm.isFeatured(im));

                    // Verifier a quelle categorie appartient l'indicateur et l'ajouter dans le
                    // tableau correspondant
                    switch (nIm.getCategory_id()) {
                        case Utils.ind_cat_environment: indCatEnvironmentList.add(nIm); break;
                        case Utils.ind_cat_social: indCatSocialList.add(nIm); break;
                        case Utils.ind_cat_good_governance: indCatGoodGovernanceList.add(nIm); break;
                        case Utils.ind_cat_economic: indCatEconomicList.add(nIm); break;
                    }
                }

                // Ajout à la liste des objets à comparer
                CompareModel compareModel = new CompareModel(new ProductModel(pm),
                        new CompareModel.CompareItemsModel(indCatEnvironmentList, indCatSocialList,
                                indCatGoodGovernanceList, indCatEconomicList),
                        pm.getCode().equals(comparedProduct.getCode()));
                compareModels.add(compareModel);

            }
        }

    }

    private void displayData() {

        // Liste des categories des indicateurs existantes
        List<IndicatorCategoryModel> indicatorCategoryModels = Utils.getIndicatorCategoryList(mContext);

        // Products
        productsTable = (TableLayout) findViewById(R.id.tab_compare);
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

        TableRow row = new TableRow(this);
        //row.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        productsTable.addView(row, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        int baseWidth = addTextToTableRow("Pro.", row);


        for (CompareModel cm : compareModels) {
            Drawable prodImg = Utils.getDrawableImage(mContext, cm.getProductModelForcompare().getImage_url());
            addImgToTableRow(prodImg, row);
        }


        // Categories
        TableLayout categoriesTable = (TableLayout) findViewById(R.id.tab_compare);

        row = new TableRow(this);
        //row.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        categoriesTable.addView(row, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        addTextToTableRow("Cat. ", row);

        for (CompareModel cm : compareModels) {
            String name = cm.getProductModelForcompare().getCategory();
            ProductCategoryModel cat = Utils.getProductCategoryByID(mContext, cm.getProductModelForcompare().getCategory());
            Drawable catImg = Utils.getDrawableImage(mContext, cat.getIcon_name());
            addImgToTableRow(catImg, row);
        }


        // Indicators
        for (IndicatorCategoryModel icm : indicatorCategoryModels) {
            row = new TableRow(this);
            categoriesTable.addView(row);

            Drawable indicatorCatImg = Utils.getDrawableImage(this, icm.getIcon_name());

            addImgToTableRow(indicatorCatImg, row);

            addTextToTableRow(icm.getName(), row, 4);

            // Environment
            if(icm.getId().equals(Utils.ind_cat_environment)) {
                // Environment indicators
                for(IndicatorModel ind : indCatEnvironmentList) {
                    row = new TableRow(this);
                    categoriesTable.addView(row);

                    Drawable indImg = Utils.getDrawableImage(this, ind.getIcon_name());
                    addImgToTableRow(indImg, row);

                    for(CompareModel cm : compareModels) {
                        if(cm.getProductModelForcompare().isFeatured(ind))
                            addTextToTableRow("V", row);
                        else
                            addTextToTableRow("X", row);
                    }
                }
            }

            // Economic well-being
            if(icm.getId().equals(Utils.ind_cat_economic)) {
                // Economic indicators
                for(IndicatorModel ind : indCatEconomicList) {
                    row = new TableRow(this);
                    categoriesTable.addView(row);

                    Drawable indImg = Utils.getDrawableImage(this, ind.getIcon_name());
                    addImgToTableRow(indImg, row);

                    for(CompareModel cm : compareModels) {
                        if(cm.getProductModelForcompare().isFeatured(ind))
                            addTextToTableRow("V", row);
                        else
                            addTextToTableRow("X", row);
                    }
                }
            }

            // Social
            if(icm.getId().equals(Utils.ind_cat_social)) {
                // Social indicators
                for(IndicatorModel ind : indCatSocialList) {
                    row = new TableRow(this);
                    categoriesTable.addView(row);

                    Drawable indImg = Utils.getDrawableImage(this, ind.getIcon_name());
                    addImgToTableRow(indImg, row);

                    for(CompareModel cm : compareModels) {
                        if(cm.getProductModelForcompare().isFeatured(ind))
                            addTextToTableRow("V", row);
                        else
                            addTextToTableRow("X", row);
                    }
                }
            }

            // Good governance
            if(icm.getId().equals(Utils.ind_cat_good_governance)) {
                // Good governance indicators
                for(IndicatorModel ind : indCatGoodGovernanceList) {
                    row = new TableRow(this);
                    categoriesTable.addView(row);

                    Drawable indImg = Utils.getDrawableImage(this, ind.getIcon_name());
                    addImgToTableRow(indImg, row);

                    for(CompareModel cm : compareModels) {
                        if(cm.getProductModelForcompare().isFeatured(ind))
                            addTextToTableRow("V", row);
                        else
                            addTextToTableRow("X", row);
                    }
                }
            }
        }
    }

    private int addTextToTableRow(String str, TableRow row)
    {
        TextView t = new TextView(this);
        TableRow.LayoutParams params =  new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
        params.setMargins(1,0,1,1);
        //t.setTextColor(color);
        t.setBackgroundColor(Color.WHITE);
        t.setGravity(Gravity.CENTER);

        t.setLayoutParams(params);
        t.setText(str);
        row.addView(t);

        return t.getMeasuredWidth();
    }

    private int addTextToTableRow(String str, TableRow row, int span)
    {
        TextView t = new TextView(this);
        TableRow.LayoutParams params =  new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
        params.setMargins(1,0,1,1);

        params.span = span;

        //t.setTextColor(color);
        t.setBackgroundColor(Color.WHITE);
        t.setGravity(Gravity.CENTER);

        t.setLayoutParams(params);
        t.setText(str);



        row.addView(t);

        return t.getWidth();
    }

    private void addImgToTableRow(Drawable img, TableRow row) {
        ImageView i = new ImageView(this);
        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(1,0,1,1);
        i.setLayoutParams(params);
        i.setBackgroundColor(Color.WHITE);
        //i.setBackground(img);
        Glide.with(mContext).load(img).apply(RequestOptions.fitCenterTransform()).into(i);
        row.addView(i);
    }

    private void addImgToTableRow(Drawable img, TableRow row, int width) {
        ImageView i = new ImageView(this);
        TableRow.LayoutParams params = new TableRow.LayoutParams(width,ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(1,0,1,1);
        i.setLayoutParams(params);
        i.setBackgroundColor(Color.WHITE);
        //i.setBackground(img);
        Glide.with(mContext).load(img).apply(RequestOptions.fitCenterTransform()).into(i);
        row.addView(i);
    }
}