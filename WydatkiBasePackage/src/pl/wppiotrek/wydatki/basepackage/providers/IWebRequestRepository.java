package pl.wppiotrek.wydatki.basepackage.providers;

import pl.wppiotrek.wydatki.basepackage.entities.ItemContainer;
import pl.wppiotrek.wydatki.basepackage.entities.ModelBase;
import pl.wppiotrek.wydatki.basepackage.entities.MultiOperationResult;
import pl.wppiotrek.wydatki.basepackage.entities.OperationResult;

interface IWebRequestRepository<T> {

	public ItemContainer<T> readAll(boolean onlyActive);

	public T readById(int id);

	public OperationResult updateActiveStates(ModelBase[] items);

	public T createOrUpdate(ModelBase item);

	public MultiOperationResult createOrUpdateMany(ModelBase[] item);

	public OperationResult delateItem(ModelBase item);
}
