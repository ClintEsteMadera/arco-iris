package commons.gui.model.bean;

import java.util.Collection;

import commons.gui.model.Attribute;
import commons.gui.model.AttributesModel;
import commons.gui.model.ValueChangeEvent;
import commons.gui.model.ValueChangeListener;
import commons.gui.model.ValueModel;
import commons.gui.model.collection.DefaultListValueModel;

/**
 * Modelo compuesto que utiliza bean-reflection para acceder a las propiedades de un objeto. Permite
 * adaptar un POJO (Plain Old Java Object), convirtiendolo en un modelo compuesto. <br>
 * <br>
 * Es un {@link commons.gui.model.ValueModel} que a su vez contiene modelos "hijos" asociados a las
 * propiedades del bean. Estos modelos tienen las siguientes características: <br>
 * <br>
 * <ul>
 * <li> En el caso de una propiedad "simple" se crea un sub-modelo que utiliza
 * {@link commons.gui.model.bean.BeanAttributes} para acceder a la propiedad. </li>
 * <br>
 * <li> En el caso de una propiedad de tipo "lista" (<code>java.util.List</code> o
 * <code>Object[]</code>) se crea un sub-modelo de tipo
 * {@link sba.ui.model.collection.ListValueModel} </li>
 * </ul>
 * @see commons.gui.model.bean.BeanAttribute
 */
public class BeanModel<T> extends AttributesModel<T> {

//	/**
//	 * Construye un modelo.
//	 * @param valueClass
//	 *            Clase de los objetos del modelo.
//	 * @param attributes
//	 *            Atributos utilizados para acceder a las propiedades.
//	 */
//	@SuppressWarnings("unchecked")
//	public BeanModel(Class<T> valueClass, Attributes attributes) {
//		super(valueClass, attributes);
//	}

//	/**
//	 * Construye un modelo.
//	 * @param editType
//	 *            Tipo de dato.
//	 * @param attributes
//	 *            Atributos utilizados para acceder a las propiedades.
//	 */
//	public BeanModel(EditType<T> editType, Attributes attributes) {
//		super(editType, attributes);
//	}

	/**
	 * Construye un modelo.
	 * @param clazz
	 *            Clase de los objetos del modelo.
	 */
	@SuppressWarnings("unchecked")
	public BeanModel(Class<T> clazz) {
		super(clazz, new BeanAttributes(clazz));
	}
	
	/**
	 * Construye un modelo.
	 * @param obj
	 *            Objeto contenido en el modelo.
	 */
	@SuppressWarnings("unchecked")
	public BeanModel(T obj) {
		this((Class<T>)obj.getClass());
		setValue(obj);
	}

	
	
	@Override
	public void setValue(T value) {
		super.setValue(value);
		
		if(value != null)
		{
			((BeanAttributes)this.getAttributes()).setPrototype(value);
		}
	}

	/**
	 * Crea el modelo "hijo" asociado a la propiedad.
	 * @param id
	 *            path de la propiedad
	 * @see AttributesModel#createValueModel(Attribute, Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected ValueModel createValueModel(final Object id) {
		final Attribute attr = getAttribute(id);
		
		if (attr != null && attr instanceof BeanAttribute) {
			final ValueModel model = new BeanAttributeValueModel((BeanAttribute) attr, this, id);
			
			//si es un valor de tipo "lista" creo un modelo de ese tipo
			//y engancho un listener
			if (DefaultListValueModel.isListConvertible(attr.getValueType().getValueClass())) {
				DefaultListValueModel listModel = new DefaultListValueModel(model);
				
				listModel.addValueChangeListener(new ValueChangeListener()
						{
							public void valueChange(ValueChangeEvent ev) {
								firePropertyChange(id);
							}
						});
				return listModel;
			}else if(Collection.class.isAssignableFrom(attr.getValueType().getValueClass())){
				//si es otro tipo de coleccion creo un modelo de tipo lista y lo conceto
				final DefaultListValueModel listModel=new DefaultListValueModel();
				
				Collection collection=(Collection)model.getValue();
				if(collection != null){
					listModel.addAll(collection);	
				}
				
				listModel.addValueChangeListener(new ValueChangeListener(){
					public void valueChange(ValueChangeEvent ev) {
						final Collection collection=(Collection)model.getValue();
						if(collection != null){
							collection.clear();
							collection.addAll(listModel.getList());	
							firePropertyChange(id);
						}
					}
				});
				return listModel;
			}
			return model;
		}
		return super.createValueModel(id);
	}

	protected Attribute createAttribute(Object key) {
		return new BeanAttribute(getValueClass(), key.toString(), this.getValue());
	}
};
