package mtws.engine;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import mtws.data.MtWsDO;

public class ModelProvider  {
	private static TreeSet<MtWsDO> aMTWSDo;
	public MtWsBO pMtWsBO;
	/**
	 * @return the pMtWsBO
	 */
	public MtWsBO getpMtWsBO() {
		return pMtWsBO;
	}

	/**
	 * @param pMtWsBO the pMtWsBO to set
	 */
	public void setpMtWsBO(MtWsBO pMtWsBO) {
		this.pMtWsBO = pMtWsBO;
	}

	static	{
		ModelProvider.aMTWSDo = new TreeSet<MtWsDO>();
	//	aMTWSBo.add(new MtWsDO("IN", "[ED,DC]", "[FW,DA]", true, false, false, false));
	//	aMTWSBo.add(new MtWsDO("FW", "[ED,DC]", "[DA]", true, false, false, false));
	}
	public ModelProvider() {
		
		pMtWsBO =   MtWsBO.getInstance();
		pMtWsBO.constructDOs();
		TreeMap<String,MtWsDO> mMtWsDO=pMtWsBO.getmMtWsDO();
		Iterator<Entry<String, MtWsDO>> i =mMtWsDO.entrySet().iterator();
		ModelProvider.aMTWSDo=new TreeSet<MtWsDO>();
		while(i.hasNext()){
			ModelProvider.aMTWSDo.add(i.next().getValue());
		}
	}

	public TreeSet<MtWsDO> getProjects() {
		return aMTWSDo;
	}


}