package pl.wppiotrek.wydatki.basepackage.webacynctasks;

import pl.wppiotrek.wydatki.basepackage.entities.ModelBase;
import pl.wppiotrek.wydatki.basepackage.enums.OperationType;
import pl.wppiotrek.wydatki.basepackage.enums.ProviderType;

public class TaskParameters {

	public ProviderType provider;
	public OperationType operation;
	public ModelBase[] items;
	public ModelBase item;

	public TaskParameters(ProviderType provider, OperationType operation) {
		this.provider = provider;
		this.operation = operation;
	}

	public TaskParameters(ProviderType provider, OperationType operation,
			ModelBase[] items) {
		this(provider, operation);
		this.items = items;
	}

	public TaskParameters(ProviderType provider, OperationType operation,
			ModelBase item) {
		this(provider, operation);
		this.item = item;
	}
}
