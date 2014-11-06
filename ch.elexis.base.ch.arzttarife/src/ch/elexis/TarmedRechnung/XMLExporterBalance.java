package ch.elexis.TarmedRechnung;

import java.util.Arrays;

import org.jdom.Element;

import ch.elexis.TarmedRechnung.XMLExporter.VatRateSum;
import ch.elexis.TarmedRechnung.XMLExporter.VatRateSum.VatRateElement;
import ch.elexis.core.constants.StringConstants;
import ch.elexis.data.Mandant;
import ch.elexis.data.Rechnung;
import ch.rgw.tools.Money;
import ch.rgw.tools.StringTool;
import ch.rgw.tools.XMLTool;

public class XMLExporterBalance {
	
	private static final String ATTR_AMOUNT_OBLIGATIONS = "amount_obligations"; //$NON-NLS-1$

	private Element balanceElement;
	
	private Money mDue;
	private Money mTotal = new Money();

	private XMLExporterBalance(Element balance){
		this.balanceElement = balance;
	}
	
	public Element getElement(){
		return balanceElement;
	}
	
	public static XMLExporterBalance buildBalance(Rechnung rechnung, XMLExporterServices services,
		VatRateSum vatSummer, XMLExporter xmlExporter){
		
		Mandant actMandant = rechnung.getMandant();

		Element element = new Element(XMLExporter.ELEMENT_BALANCE, XMLExporter.nsinvoice);
		XMLExporterBalance balance = new XMLExporterBalance(element);
		
		String curr =
			(String) actMandant.getRechnungssteller().getExtInfoStoredObjectByKey(
				Messages.XMLExporter_Currency);
		if (StringTool.isNothing(curr)) {
			curr = "CHF"; //$NON-NLS-1$
		}
		element.setAttribute("currency", curr);
		
		balance.mTotal.addMoney(services.getTarmedMoney()).addMoney(services.getAnalysenMoney())
			.addMoney(services.getMedikamentMoney()).addMoney(services.getUebrigeMoney())
			.addMoney(services.getKantMoney()).addMoney(services.getPhysioMoney())
			.addMoney(services.getMigelMoney());

		element.setAttribute(XMLExporter.ATTR_AMOUNT, XMLTool.moneyToXmlDouble(balance.mTotal));
		balance.mDue = new Money(balance.mTotal);
		balance.mDue.subtractMoney(rechnung.getAnzahlung());
		balance.mDue.roundTo5();
		
		// round and create Money from sumTarmed double values
		Money mTarmedAL = new Money((int) Math.round(services.getSumTarmedAL()));
		Money mTarmedTL = new Money((int) Math.round(services.getSumTarmedTL()));

		element.setAttribute(XMLExporter.ATTR_AMOUNT_DUE, XMLTool.moneyToXmlDouble(balance.mDue));
		element.setAttribute(XMLExporter.ATTR_AMOUNT_TARMED,
			XMLTool.moneyToXmlDouble(services.getTarmedMoney()));
		element
			.setAttribute(XMLExporter.ATTR_AMOUNT_TARMED_MT, XMLTool.moneyToXmlDouble(mTarmedAL));
		element
			.setAttribute(XMLExporter.ATTR_AMOUNT_TARMED_TT, XMLTool.moneyToXmlDouble(mTarmedTL));
		element.setAttribute(XMLExporter.ATTR_AMOUNT_CANTONAL, StringConstants.DOUBLE_ZERO);
		element.setAttribute(XMLExporter.ATTR_AMOUNT_UNCLASSIFIED,
			XMLTool.moneyToXmlDouble(services.getUebrigeMoney()));
		element.setAttribute(XMLExporter.ATTR_AMOUNT_LAB,
			XMLTool.moneyToXmlDouble(services.getAnalysenMoney()));
		element.setAttribute(XMLExporter.ATTR_AMOUNT_PHYSIO,
			XMLTool.moneyToXmlDouble(services.getPhysioMoney()));
		element.setAttribute(XMLExporter.ATTR_AMOUNT_DRUG,
			XMLTool.moneyToXmlDouble(services.getMedikamentMoney()));
		element.setAttribute(XMLExporter.ATTR_AMOUNT_MIGEL,
			XMLTool.moneyToXmlDouble(services.getMigelMoney()));
		element.setAttribute(ATTR_AMOUNT_OBLIGATIONS, XMLTool.moneyToXmlDouble(balance.mTotal));
		
		// 10370 Vat on invoice level
		Element vat = new Element(XMLExporter.ELEMENT_VAT, XMLExporter.nsinvoice);
		
		String vatNumber =
			actMandant.getRechnungssteller().getInfoString(XMLExporter.VAT_MANDANTVATNUMBER);
		if (vatNumber != null && vatNumber.length() > 0)
			vat.setAttribute(XMLExporter.ELEMENT_VAT_NUMBER, vatNumber);
		
		vat.setAttribute(XMLExporter.ELEMENT_VAT, XMLTool.doubleToXmlDouble(vatSummer.sumvat, 2));
		
		// 10380 Vat on rate level
		VatRateElement[] vatValues = vatSummer.rates.values().toArray(new VatRateElement[0]);
		Arrays.sort(vatValues);
		for (VatRateElement rate : vatValues) {
			Element vatrate = new Element(XMLExporter.ATTR_VAT_RATE, XMLExporter.nsinvoice);
			vatrate.setAttribute(XMLExporter.ATTR_VAT_RATE,
				XMLTool.doubleToXmlDouble(rate.scale, 2));
			vatrate.setAttribute(XMLExporter.ATTR_AMOUNT,
				XMLTool.doubleToXmlDouble(rate.sumamount, 2));
			vatrate
				.setAttribute(XMLExporter.ELEMENT_VAT, XMLTool.doubleToXmlDouble(rate.sumvat, 2));
			vat.addContent(vatrate);
		}
		
		element.addContent(vat);
		
		return balance;
	}
	
	public Money getDue(){
		return mDue;
	}
	
	public Money getTotal(){
		return mTotal;
	}
}
