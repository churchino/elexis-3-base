package at.medevit.elexis.medicationlist.ui.composites;

import java.util.List;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import at.medevit.elexis.medicationlist.ui.PreferenceConstants;
import ch.elexis.core.constants.StringConstants;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.data.events.ElexisEventDispatcher;
import ch.elexis.core.model.IPersistentObject;
import ch.elexis.core.ui.icons.ImageSize;
import ch.elexis.core.ui.icons.Images;
import ch.elexis.data.Artikel;
import ch.elexis.data.PersistentObject;
import ch.elexis.data.Prescription;
import ch.elexis.data.Rezept;
import ch.elexis.data.Verrechnet;
import ch.rgw.tools.TimeTool;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class MedicationComposite extends Composite {
	
	private Composite compositeMedicationDetail;
	private Composite compositeDosage;
	private Text txtMorning, txtNoon, txtEvening, txtNight;
	private Button btnPRNMedication;
	private Composite compositeMedicationTable;
	private TableViewer medicationTableViewer;
	
	private IChangeListener listener;
	private Button btnIsFixmedication;
	private GridData compositeMedicationDetailLayoutData;
	private MovePrescriptionPositionInTableUpAction mppita_up;
	private MovePrescriptionPositionInTableDownAction mppita_down;
	private Button btnConfirm;
	
	private String[] signatureArray;
	private Button btnShowHistory;
	private StackLayout stackLayout;
	private Composite compositeMedicationTextDetails;
	private Composite compositeStopMedicationTextDetails;
	private Composite stackedMedicationDetailComposite;
	private TableViewerColumn tableViewerColumnStop;
	
	private DataBindingContext dbc = new DataBindingContext();
	private WritableValue selectedMedication = new WritableValue(null, Prescription.class);
	private WritableValue lastDisposalPO = new WritableValue(null, PersistentObject.class);
	private TableColumnLayout tcl_compositeMedicationTable = new TableColumnLayout();
	private Button btnStopMedication;
	private Label lblLastDisposalLink;
	
	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public MedicationComposite(Composite parent, int style){
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		medicationTableComposite();
		stateComposite();
		medicationDetailComposite();
		registerDatabindingUpdateListener();
		
//		showMedicationDetailComposite(null);
	}
	
	private void registerDatabindingUpdateListener(){
		listener = new IChangeListener() {
			@Override
			public void handleChange(ChangeEvent event){
				medicationTableViewer.getTable().getDisplay().asyncExec(new Runnable() {
					
					@Override
					public void run(){
						medicationTableViewer.refresh();
					}
				});
			}
		};
		
		IObservableList bindings = dbc.getValidationStatusProviders();
		
		for (Object o : bindings) {
			Binding b = (Binding) o;
			b.getTarget().addChangeListener(listener);
		}
	}
	
	@Override
	public void dispose(){
		IObservableList providers = dbc.getValidationStatusProviders();
		for (Object o : providers) {
			Binding b = (Binding) o;
			b.getTarget().removeChangeListener(listener);
		}
		dbc.dispose();
		super.dispose();
	}
	
	private void medicationTableComposite(){
		compositeMedicationTable = new Composite(this, SWT.NONE);
		compositeMedicationTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeMedicationTable.setLayout(tcl_compositeMedicationTable);
		
		// ------ MAIN TABLE VIEWER -----------------------------
		medicationTableViewer =
			new TableViewer(compositeMedicationTable, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		Table medicationTable = medicationTableViewer.getTable();
		medicationTable.setHeaderVisible(true);
		medicationTableViewer.setSorter(new MedicationTableViewerSorter(medicationTableViewer));
		medicationTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e){
				IStructuredSelection is =
					(IStructuredSelection) medicationTableViewer.getSelection();
				Prescription presc = (Prescription) is.getFirstElement();
				
				// set last disposition information
				IPersistentObject po = presc.getLastDisposed();
				lastDisposalPO.setValue(po);
				if (po != null) {
					String label = "";
					if (po instanceof Rezept) {
						Rezept rp = (Rezept) po;
						label = "Rezept vom " + rp.getDate();
					} else if (po instanceof Verrechnet) {
						Verrechnet v = (Verrechnet) po;
						label = "Kons vom " + v.getKons().getDatum();
					}
					lblLastDisposalLink.setText(label);
				} else {
					lblLastDisposalLink.setText("");
				}
				
				// set writable databinding value
				selectedMedication.setValue(presc);
				// update medication detailcomposite
				showMedicationDetailComposite(presc);
				ElexisEventDispatcher.fireSelectionEvent(presc);
				
				signatureArray = Prescription.getSignatureAsStringArray(presc.getDosis());
				setValuesForTextSignatureArray(signatureArray);
				
				super.widgetSelected(e);
			}
		});
		medicationTableViewer.addFilter(new ViewerFilter() {
			
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element){
				if (btnShowHistory.getSelection())
					return true;
				
				return MedicationCellLabelProvider.isNotHistorical((Prescription) element);
			}
		});
		
		mppita_up = new MovePrescriptionPositionInTableUpAction(medicationTableViewer);
		mppita_down = new MovePrescriptionPositionInTableDownAction(medicationTableViewer);
		medicationTableViewer.getTable().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e){
				if (e.stateMask == SWT.COMMAND
					&& (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN)) {
					if (e.keyCode == SWT.ARROW_UP) {
						mppita_up.run();
					} else if (e.keyCode == SWT.ARROW_DOWN) {
						mppita_down.run();
					}
					e.doit = false;
				} else {
					super.keyPressed(e);
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e){
				if (e.stateMask == SWT.COMMAND
					&& (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN))
					return;
				super.keyReleased(e);
			}
		});
		// ------ END MAIN TABLE VIEWER -----------------------------
		
		// article
		TableViewerColumn tableViewerColumnArticle =
			new TableViewerColumn(medicationTableViewer, SWT.NONE);
		TableColumn tblclmnArticle = tableViewerColumnArticle.getColumn();
		tcl_compositeMedicationTable.setColumnData(tblclmnArticle, new ColumnPixelData(170, true,
			true));
		tblclmnArticle.setText(Messages.TherapieplanComposite_tblclmnArticle_text);
		tableViewerColumnArticle.setLabelProvider(new MedicationCellLabelProvider() {
			
			@Override
			public String getText(Object element){
				Prescription pres = (Prescription) element;
				String label = "??";
				if (pres.getArtikel() != null) {
					Artikel art = pres.getArtikel();
					label = art.getLabel();
					// icons -> suchtgift, generica, nicht spezialitätenliste
				}
				return label;
			}
		});
		
		// package size
		TableViewerColumn tableViewerColumnPackage =
			new TableViewerColumn(medicationTableViewer, SWT.NONE);
		TableColumn tblclmnPackage = tableViewerColumnPackage.getColumn();
		tblclmnPackage.setAlignment(SWT.CENTER);
		tcl_compositeMedicationTable.setColumnData(tblclmnPackage, new ColumnPixelData(8, true,
			true));
		tblclmnPackage.setText("#");
		tableViewerColumnPackage.setLabelProvider(new MedicationCellLabelProvider() {
			
			@Override
			public Image getImage(Object element){
				Prescription pres = (Prescription) element;
				if (!pres.isAppliedMedication())
					return null;
				return Images.IMG_SYRINGE.getImage();
			}
			
			@Override
			public String getText(Object element){
				Prescription pres = (Prescription) element;
				if (pres.isAppliedMedication())
					return "";
				String label =
					(pres.getArtikel() != null) ? pres.getArtikel().getPackungsGroesse() + ""
							: "??";
				return label;
			}
		});
		
		// dosage
		TableViewerColumn tableViewerColumnDosage =
			new TableViewerColumn(medicationTableViewer, SWT.NONE);
		tableViewerColumnDosage.setLabelProvider(new MedicationCellLabelProvider() {
			
			@Override
			public String getText(Object element){
				Prescription pres = (Prescription) element;
				String dosis = pres.getDosis();
				return (dosis.equals(StringConstants.ZERO) ? "gestoppt" : dosis);
			}
		});
		TableColumn tblclmnDosage = tableViewerColumnDosage.getColumn();
		tblclmnDosage.setToolTipText(Messages.TherapieplanComposite_tblclmnDosage_toolTipText);
		tcl_compositeMedicationTable.setColumnData(tblclmnDosage, new ColumnPixelData(40, true,
			true));
		tableViewerColumnDosage.getColumn().setText(
			Messages.TherapieplanComposite_tblclmnDosage_text);
		
		// enacted
		TableViewerColumn tableViewerColumnEnacted =
			new TableViewerColumn(medicationTableViewer, SWT.CENTER);
		TableColumn tblclmnEnacted = tableViewerColumnEnacted.getColumn();
		tcl_compositeMedicationTable.setColumnData(tblclmnEnacted, new ColumnPixelData(45, true,
			true));
		tblclmnEnacted.setImage(Images.resize(Images.IMG_NEXT_WO_SHADOW.getImage(),
			ImageSize._12x12_TableColumnIconSize));
		tableViewerColumnEnacted.setLabelProvider(new MedicationCellLabelProvider() {
			
			@Override
			public String getText(Object element){
				Prescription pres = (Prescription) element;
				TimeTool tt = new TimeTool(pres.getBeginDate());
				return tt.toString(TimeTool.DATE_GER_SHORT);
			}
		});
		
		// supplied until
		TableViewerColumn tableViewerColumnSuppliedUntil =
			new TableViewerColumn(medicationTableViewer, SWT.NONE);
		TableColumn tblclmnSufficient = tableViewerColumnSuppliedUntil.getColumn();
		tcl_compositeMedicationTable.setColumnData(tblclmnSufficient, new ColumnPixelData(10, true,
			true));
		tblclmnSufficient.setText("");
		tableViewerColumnSuppliedUntil.setLabelProvider(new MedicationCellLabelProvider() {
			
			@Override
			public String getText(Object element){
				Prescription pres = (Prescription) element;
				if (!pres.isFixedMediation() || pres.isAsNeededMedication())
					return "";
				
				TimeTool tt = pres.getSuppliedUntilDate();
				if (tt != null && tt.isAfterOrEqual(new TimeTool())) {
					return "OK";
				}
				
				return "?";
			}
		});
		
		// comment
		TableViewerColumn tableViewerColumnComment =
			new TableViewerColumn(medicationTableViewer, SWT.NONE);
		TableColumn tblclmnComment = tableViewerColumnComment.getColumn();
		tcl_compositeMedicationTable.setColumnData(tblclmnComment, new ColumnWeightData(1,
			ColumnWeightData.MINIMUM_WIDTH, true));
		tblclmnComment.setText("comment");
		tableViewerColumnComment.setLabelProvider(new MedicationCellLabelProvider() {
			
			@Override
			public String getText(Object element){
				Prescription pres = (Prescription) element;
				return pres.getBemerkung();
			}
		});
		tableViewerColumnComment.setEditingSupport(new PersistentObjectFieldEditingSupport(
			medicationTableViewer, Prescription.FLD_REMARK));
		
		medicationTableViewer.setContentProvider(ArrayContentProvider.getInstance());
	}
	
	private void createStopTableViewerColumn(int index){
		tableViewerColumnStop = new TableViewerColumn(medicationTableViewer, SWT.CENTER, index);
		TableColumn tblclmnStop = tableViewerColumnStop.getColumn();
		ColumnPixelData stopColumnPixelData = new ColumnPixelData(45, true, true);
		tcl_compositeMedicationTable.setColumnData(tblclmnStop, stopColumnPixelData);
		tblclmnStop.setImage(Images.resize(Images.IMG_ARROWSTOP_WO_SHADOW.getImage(),
			ImageSize._12x12_TableColumnIconSize));
		tableViewerColumnStop.setLabelProvider(new MedicationCellLabelProvider() {
			
			@Override
			public String getText(Object element){
				Prescription pres = (Prescription) element;
				String endDate = pres.getEndDate();
				if (endDate != null && endDate.length() > 4) {
					TimeTool tt = new TimeTool(pres.getEndDate());
					return tt.toString(TimeTool.DATE_GER_SHORT);
				} else {
					return "";
				}
			}
		});
	}
	
	private void stateComposite(){
		Composite compositeState = new Composite(this, SWT.NONE);
		GridLayout gl_compositeState = new GridLayout(6, false);
		gl_compositeState.marginWidth = 0;
		gl_compositeState.marginHeight = 0;
		gl_compositeState.horizontalSpacing = 0;
		compositeState.setLayout(gl_compositeState);
		compositeState.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		
		Label lblLastDisposal = new Label(compositeState, SWT.NONE);
		lblLastDisposal.setText("Letzte Abgabe: ");
		
		lblLastDisposalLink = new Label(compositeState, SWT.NONE);
		lblLastDisposalLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e){
				PersistentObject po = (PersistentObject) lastDisposalPO.getValue();
				if (po == null)
					return;
				
				System.out.println(po);
			}
		});
		lblLastDisposalLink.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		lblLastDisposalLink.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDailyTherapyCost = new Label(compositeState, SWT.NONE);
		lblDailyTherapyCost.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblDailyTherapyCost.setText(Messages.MedicationComposite_lblNewLabel_text);
		
		btnShowHistory = new Button(compositeState, SWT.CHECK);
		btnShowHistory.setToolTipText(Messages.MedicationComposite_btnShowHistory_toolTipText);
		btnShowHistory.setText(Messages.MedicationComposite_btnCheckButton_text);
		btnShowHistory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e){
				if (btnShowHistory.getSelection()) {
					createStopTableViewerColumn(4);
				} else {
					tableViewerColumnStop.getColumn().dispose();
				}
				medicationTableViewer.refresh();
				compositeMedicationTable.layout(true);
			}
		});
		
		ComboViewer comboViewerSortOrder = new ComboViewer(compositeState, SWT.None);
		comboViewerSortOrder.setContentProvider(ArrayContentProvider.getInstance());
		comboViewerSortOrder.setInput(ComboViewerSortOrder.values());
		comboViewerSortOrder.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element){
				ComboViewerSortOrder cvso = (ComboViewerSortOrder) element;
				return cvso.label;
			}
		});
		comboViewerSortOrder.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event){
				StructuredSelection ss = (StructuredSelection) event.getSelection();
				ComboViewerSortOrder order = (ComboViewerSortOrder) ss.getFirstElement();
				medicationTableViewer.setComparator(order.vc);
				CoreHub.userCfg.set(PreferenceConstants.PREF_MEDICATIONLIST_SORT_ORDER, order.val);
			}
		});
		int sorter = CoreHub.userCfg.get(PreferenceConstants.PREF_MEDICATIONLIST_SORT_ORDER, 1);
		comboViewerSortOrder.setSelection(new StructuredSelection(ComboViewerSortOrder
			.getSortOrderPerValue(sorter)));
		
		Label lblSortInfo = new Label(compositeState, SWT.NONE);
		lblSortInfo.setBounds(0, 0, 60, 14);
		lblSortInfo.setText(Messages.MedicationComposite_lblNewLabel_text_1);
	}
	
	private void medicationDetailComposite(){
		compositeMedicationDetail = new Composite(this, SWT.BORDER);
		GridLayout gl_compositeMedicationDetail = new GridLayout(5, false);
		gl_compositeMedicationDetail.marginBottom = 5;
		gl_compositeMedicationDetail.marginRight = 5;
		gl_compositeMedicationDetail.marginLeft = 5;
		gl_compositeMedicationDetail.marginTop = 5;
		gl_compositeMedicationDetail.marginWidth = 0;
		gl_compositeMedicationDetail.marginHeight = 0;
		compositeMedicationDetail.setLayout(gl_compositeMedicationDetail);
		compositeMedicationDetailLayoutData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		compositeMedicationDetail.setLayoutData(compositeMedicationDetailLayoutData);
		
		{
			compositeDosage = new Composite(compositeMedicationDetail, SWT.NONE);
			compositeDosage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			GridLayout gl_compositeDosage = new GridLayout(7, false);
			gl_compositeDosage.marginWidth = 0;
			gl_compositeDosage.marginHeight = 0;
			gl_compositeDosage.verticalSpacing = 1;
			gl_compositeDosage.horizontalSpacing = 0;
			compositeDosage.setLayout(gl_compositeDosage);
			
			txtMorning = new Text(compositeDosage, SWT.BORDER);
			txtMorning.setTextLimit(6);
			txtMorning.setMessage("morn");
			GridData gd_txtMorning = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
			gd_txtMorning.widthHint = 40;
			txtMorning.setLayoutData(gd_txtMorning);
			txtMorning.addModifyListener(new SignatureArrayModifyListener(0));
			
			Label lblStop = new Label(compositeDosage, SWT.HORIZONTAL);
			lblStop.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblStop.setText("-");
			
			txtNoon = new Text(compositeDosage, SWT.BORDER);
			txtNoon.setTextLimit(6);
			txtNoon.setMessage("noon");
			GridData gd_txtNoon = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
			gd_txtNoon.widthHint = 40;
			txtNoon.setLayoutData(gd_txtNoon);
			txtNoon.addModifyListener(new SignatureArrayModifyListener(1));
			
			Label lblStop2 = new Label(compositeDosage, SWT.NONE);
			lblStop2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblStop2.setText("-");
			
			txtEvening = new Text(compositeDosage, SWT.BORDER);
			txtEvening.setTextLimit(6);
			txtEvening.setMessage("eve");
			GridData gd_txtEvening = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
			gd_txtEvening.widthHint = 40;
			txtEvening.setLayoutData(gd_txtEvening);
			txtEvening.addModifyListener(new SignatureArrayModifyListener(2));
			
			Label lblStop3 = new Label(compositeDosage, SWT.NONE);
			lblStop3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblStop3.setText("-");
			
			txtNight = new Text(compositeDosage, SWT.BORDER);
			txtNight.setTextLimit(6);
			txtNight.setMessage("night");
			GridData gd_txtNight = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
			gd_txtNight.widthHint = 40;
			txtNight.setLayoutData(gd_txtNight);
			txtNight.addModifyListener(new SignatureArrayModifyListener(3));
		}
		
		Composite compositeFixmedication = new Composite(compositeMedicationDetail, SWT.NONE);
		compositeFixmedication.setBackground(SWTResourceManager.getColor(238, 232, 170));
		compositeFixmedication.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		GridLayout gl_compositeFixmedication = new GridLayout(1, false);
		gl_compositeFixmedication.marginRight = 2;
		gl_compositeFixmedication.marginTop = 2;
		gl_compositeFixmedication.horizontalSpacing = 0;
		gl_compositeFixmedication.verticalSpacing = 0;
		gl_compositeFixmedication.marginWidth = 0;
		gl_compositeFixmedication.marginHeight = 0;
		compositeFixmedication.setLayout(gl_compositeFixmedication);
		
		btnIsFixmedication = new Button(compositeFixmedication, SWT.CHECK);
		btnIsFixmedication.setToolTipText(Messages.MedicationComposite_btnIsFixmedication_toolTipText);
		btnIsFixmedication.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		btnIsFixmedication.setText(Messages.TherapieplanComposite_btnIsFixmedication_text);
		IObservableValue uiObs = WidgetProperties.selection().observe(btnIsFixmedication);
		IObservableValue prescObs =
			PojoProperties.value("fixedMedication", Boolean.class)
				.observeDetail(selectedMedication);
		dbc.bindValue(uiObs, prescObs);

		Composite compositePRNMedication = new Composite(compositeMedicationDetail, SWT.NONE);
		compositePRNMedication.setBackground(SWTResourceManager.getColor(102, 205, 170));
		compositePRNMedication.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		GridLayout gl_compositePRNMedication = new GridLayout(1, false);
		gl_compositePRNMedication.marginRight = 2;
		gl_compositePRNMedication.marginTop = 2;
		gl_compositePRNMedication.horizontalSpacing = 0;
		gl_compositePRNMedication.verticalSpacing = 0;
		gl_compositePRNMedication.marginWidth = 0;
		gl_compositePRNMedication.marginHeight = 0;
		compositePRNMedication.setLayout(gl_compositePRNMedication);
		
		btnPRNMedication = new Button(compositePRNMedication, SWT.CHECK);
		btnPRNMedication.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		btnPRNMedication
			.setToolTipText(Messages.TherapieplanComposite_btnPRNMedication_toolTipText);
		btnPRNMedication.setText(Messages.MedicationComposite_btnPRNMedication_text);
		IObservableValue uiprnObs = WidgetProperties.selection().observe(btnPRNMedication);
		IObservableValue prescprnObs =
			PojoProperties.value("asNeededMedication", Boolean.class).observeDetail(
				selectedMedication);
		dbc.bindValue(uiprnObs, prescprnObs);
		
		btnStopMedication = new Button(compositeMedicationDetail, SWT.FLAT | SWT.TOGGLE);
		btnStopMedication.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnStopMedication.setText(Messages.MedicationComposite_btnNewButton_text_1);
		btnStopMedication.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e){
				if (btnStopMedication.getSelection()) {
					stackLayout.topControl = compositeStopMedicationTextDetails;
				} else {
					stackLayout.topControl = compositeMedicationTextDetails;
				}
				btnConfirm.setEnabled(btnStopMedication.getSelection());
				stackedMedicationDetailComposite.layout();
			}
		});
		
		btnConfirm = new Button(compositeMedicationDetail, SWT.FLAT);
		btnConfirm.setText(Messages.MedicationComposite_btnNewButton_text);
		btnConfirm.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e){
				Prescription pres = (Prescription) selectedMedication.getValue();
				if (pres == null)
					return; // prevent npe
				String newDose;
				
				if (btnStopMedication.getSelection()) {
					// stop medication
					newDose = StringConstants.ZERO;
				} else {
					// change signature
					newDose = getDosisStringFromSignatureTextArray();
				}
				
				setValuesForTextSignatureArray(Prescription.getSignatureAsStringArray(newDose));
				pres.addTerm(null, newDose);
				btnConfirm.setEnabled(false);
				medicationTableViewer.refresh();
			}
		});
		btnConfirm.setEnabled(false);
		
		stackedMedicationDetailComposite = new Composite(compositeMedicationDetail, SWT.NONE);
		stackedMedicationDetailComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
			false, 5, 1));
		stackLayout = new StackLayout();
		stackedMedicationDetailComposite.setLayout(stackLayout);
		
		// --- medication detail
		compositeMedicationTextDetails = new Composite(stackedMedicationDetailComposite, SWT.NONE);
		GridLayout gl_compositeMedicationTextDetails = new GridLayout(1, false);
		gl_compositeMedicationTextDetails.marginWidth = 0;
		gl_compositeMedicationTextDetails.horizontalSpacing = 0;
		gl_compositeMedicationTextDetails.marginHeight = 0;
		compositeMedicationTextDetails.setLayout(gl_compositeMedicationTextDetails);
		compositeMedicationTextDetails.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
			5, 1));
		
		Text txtIntakeOrder = new Text(compositeMedicationTextDetails, SWT.BORDER);
		txtIntakeOrder.setMessage(Messages.MedicationComposite_txtIntakeOrder_message);
		txtIntakeOrder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		IObservableValue txtIntakeOrderObservable =
			WidgetProperties.text(SWT.Modify).observeDelayed(100, txtIntakeOrder);
		IObservableValue intakeOrderObservable =
			PojoProperties.value("bemerkung", String.class).observeDetail(selectedMedication);
		dbc.bindValue(txtIntakeOrderObservable, intakeOrderObservable);
		
		Text txtDisposalComment = new Text(compositeMedicationTextDetails, SWT.BORDER);
		txtDisposalComment.setMessage(Messages.MedicationComposite_txtComment_message);
		txtDisposalComment.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		IObservableValue txtCommentObservable =
			WidgetProperties.text(SWT.Modify).observeDelayed(100, txtDisposalComment);
		IObservableValue commentObservable =
			PojoProperties.value("disposalComment", String.class).observeDetail(selectedMedication);
		dbc.bindValue(txtCommentObservable, commentObservable);
		
		stackLayout.topControl = compositeMedicationTextDetails;
		
		// --- stop medication detail
		compositeStopMedicationTextDetails =
			new Composite(stackedMedicationDetailComposite, SWT.NONE);
		GridLayout gl_compositeStopMedicationTextDetails = new GridLayout(1, false);
		gl_compositeStopMedicationTextDetails.marginWidth = 0;
		gl_compositeStopMedicationTextDetails.horizontalSpacing = 0;
		gl_compositeStopMedicationTextDetails.marginHeight = 0;
		compositeStopMedicationTextDetails.setLayout(gl_compositeStopMedicationTextDetails);
		compositeStopMedicationTextDetails.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
			false, 5, 1));
		
		Text txtStopComment = new Text(compositeStopMedicationTextDetails, SWT.BORDER);
		txtStopComment.setMessage("Grund des Stopps");
		txtStopComment.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		IObservableValue txtStopCommentObservableUi =
			WidgetProperties.text(SWT.Modify).observeDelayed(100, txtStopComment);
		IObservableValue txtStopCommentObservable =
			PojoProperties.value("stopReason", String.class).observeDetail(selectedMedication);
		dbc.bindValue(txtStopCommentObservableUi, txtStopCommentObservable);
		
		Text txtIntolerance = new Text(compositeStopMedicationTextDetails, SWT.BORDER);
		txtIntolerance.setMessage("Unverträglichkeit");
		txtIntolerance.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		//		IObservableValue txtCommentObservable =
		//			WidgetProperties.text(SWT.Modify).observeDelayed(100, txtDisposalComment);
		//		IObservableValue commentObservable =
		//			PojoProperties.value("disposalComment", String.class).observeDetail(selectedMedication);
		//		dbc.bindValue(txtCommentObservable, commentObservable);
	}
	
	/**
	 * show the medication detail composite; if the medication is stopped, show the stop relevant
	 * information, else show the default information
	 * 
	 * A medication can not be stopped, if it is not a fixed medication, or was already stopped
	 * 
	 * @param presc
	 */
	private void showMedicationDetailComposite(Prescription presc){
		boolean showDetailComposite = (presc != null && !presc.isAppliedMedication());
		
		compositeMedicationDetail.setVisible(showDetailComposite);
		compositeMedicationDetailLayoutData.exclude = !(showDetailComposite);
		
		if (showDetailComposite) {
			boolean stopped = presc.get(Prescription.FLD_DATE_UNTIL).length() > 1;
			if (stopped) {
				stackLayout.topControl = compositeStopMedicationTextDetails;
			} else {
				stackLayout.topControl = compositeMedicationTextDetails;
			}
			btnStopMedication.setEnabled(!stopped && presc.isFixedMediation());
			stackedMedicationDetailComposite.layout();
		}
		
		this.layout(true);
	}
	
	@Override
	protected void checkSubclass(){
		// Disable the check that prevents subclassing of SWT components
	}
	
	public void updateUi(final List<Prescription> prescriptionList){
		selectedMedication.setValue(null);
		medicationTableViewer.setInput(prescriptionList);
		
	}
	
	public TableViewer getMedicationTableViewer(){
		return medicationTableViewer;
	}
	
	private void setValuesForTextSignatureArray(String[] signatureArray){
		txtMorning.setText(signatureArray[0]);
		txtNoon.setText(signatureArray[1]);
		txtEvening.setText(signatureArray[2]);
		txtNight.setText(signatureArray[3]);
	}
	
	/**
	 * @return the values in the signature text array as dose string
	 */
	private String getDosisStringFromSignatureTextArray(){
		String[] values = new String[4];
		values[0] = txtMorning.getText();
		values[1] = txtNoon.getText();
		values[2] = txtEvening.getText();
		values[3] = txtNight.getText();
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			String string = values[i];
			if (string.length() > 0) {
				if (i > 0) {
					sb.append("-");
				}
				sb.append(string);
			}
		}
		return sb.toString();
	}
	
	/**
	 * detects a change in the signature text array (i.e. txtMorning, txtNoon, txtEvening, txtNight)
	 */
	private class SignatureArrayModifyListener implements ModifyListener {
		
		final int index;
		
		public SignatureArrayModifyListener(int index){
			this.index = index;
		}
		
		@Override
		public void modifyText(ModifyEvent e){
			String text = ((Text) e.getSource()).getText();
			btnConfirm.setEnabled(!signatureArray[index].equals(text));
		}
	}
}
