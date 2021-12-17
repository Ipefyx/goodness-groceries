package lu.uni.bicslab.greenbot.android.other;

import java.util.List;

import lu.uni.bicslab.greenbot.android.datamodel.IndicatorModel;
import lu.uni.bicslab.greenbot.android.datamodel.ProductModel;

public class CompareModel {
	
	ProductModel productModelForcompare;
	CompareItemsModel mCompareItemsModel;
	boolean isReferenceProduct;

	public CompareModel(ProductModel producModel, CompareItemsModel compareItemsModel, boolean isRef) {
		this.productModelForcompare = producModel;
		this.mCompareItemsModel = compareItemsModel;
		this.isReferenceProduct = isRef;
	}
	
	public ProductModel getProductModelForcompare() {
		return productModelForcompare;
	}
	
	public void setProductModelForcompare(ProductModel productModelForcompare) {
		this.productModelForcompare = productModelForcompare;
	}
	
	public CompareItemsModel getmCompareItemsModel() {
		return mCompareItemsModel;
	}
	
	public void setmCompareItemsModel(CompareItemsModel mCompareItemsModel) {
		this.mCompareItemsModel = mCompareItemsModel;
	}

	public boolean IsReference() { return isReferenceProduct; }
	
	public static class CompareItemsModel {
		List<IndicatorModel> indCatEnvironmentlist;
		List<IndicatorModel> indCatSociallist;
		List<IndicatorModel> indCatGoodGevernanceList;
		List<IndicatorModel> indCatEconomicList;
		
		public CompareItemsModel(List<IndicatorModel> indCatEnvironmentlist, List<IndicatorModel> indCatSociallist, List<IndicatorModel> indCatGoodGevernanceList, List<IndicatorModel> indCatEconomicList) {
			this.indCatEnvironmentlist = indCatEnvironmentlist;
			this.indCatSociallist = indCatSociallist;
			this.indCatGoodGevernanceList = indCatGoodGevernanceList;
			this.indCatEconomicList = indCatEconomicList;
		}
		
		public List<IndicatorModel> getIndCatEnvironmentlist() {
			return indCatEnvironmentlist;
		}
		
		public void setIndCatEnvironmentlist(List<IndicatorModel> indCatEnvironmentlist) {
			this.indCatEnvironmentlist = indCatEnvironmentlist;
		}
		
		public List<IndicatorModel> getIndCatSociallist() {
			return indCatSociallist;
		}
		
		public void setIndCatSociallist(List<IndicatorModel> indCatSociallist) {
			this.indCatSociallist = indCatSociallist;
		}
		
		public List<IndicatorModel> getIndCatGoodGevernanceList() {
			return indCatGoodGevernanceList;
		}
		
		public void setIndCatGoodGevernanceList(List<IndicatorModel> indCatGoodGevernanceList) {
			this.indCatGoodGevernanceList = indCatGoodGevernanceList;
		}
		
		public List<IndicatorModel> getIndCatEconomicList() {
			return indCatEconomicList;
		}
		
		public void setIndCatEconomicList(List<IndicatorModel> indCatEconomicList) {
			this.indCatEconomicList = indCatEconomicList;
		}
	}
}
