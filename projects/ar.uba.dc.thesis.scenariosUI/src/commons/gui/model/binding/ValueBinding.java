package commons.gui.model.binding;

import commons.gui.model.ValueModel;

public interface ValueBinding {

		/**
		 * Agrega un listener para el evento de lectura (componente --> modelo)
		 * 
		 * @param listener
		 */
        void addReadingListener(ValueBindingUpdateListener listener);
        
        /**
         * Elimina un listener para el evento de lectura.
         * 
         * @param listener
         */
        void removeReadingListener(ValueBindingUpdateListener listener);

        /**
         * Agrega un listener para el evento de escritura (modelo --> componente)
         * 
         * @param listener
         */
        void addWritingListener(ValueBindingUpdateListener listener);
        
        /**
         * Elimina un listener para el evento de escritura.
         * 
         * @param listener
         */
        void removeWritingListener(ValueBindingUpdateListener listener);

        /**
         * Obtiene el ValueModel.
         * 
         * @return
         */
        ValueModel getValueModel();
        
        /**
         * Obtiene el componetne.
         * 
         * @return
         */
        Object getComponent();

        /**
         * Desactiva el binding.
         *
         */
        void unbind();

        /**
         * Activa el binding.
         *
         */
        void bind();

        /**
         * Actualiza explícitamente el ValueModel con el valor del componente.
         */
        void read();

        /**
         * Actualiza explicitamente el componente con el valor del ValueModel
         */
        void write();
}
