from sklearn.compose import ColumnTransformer
from sklearn.pipeline import Pipeline
from sklearn.impute import SimpleImputer
from sklearn.preprocessing import OneHotEncoder, StandardScaler
from sklearn.metrics import mean_absolute_error


def get_pipeline(model, X):
	numerical_features = list(X.columns[X.dtypes == 'int64'])
	continuous_features = list(X.columns[X.dtypes == 'float64'])
	categorical_features = list(X.columns[X.dtypes == 'object'])

	print(f'columns: numerical-continuous-categorical: {len(numerical_features)}-{len(continuous_features)}-{len(categorical_features)}')
	preprocessor = __get_preprocessor(numerical_features + continuous_features, categorical_features)

	# Bundle preprocessing and modeling code in a pipeline
	my_pipeline = Pipeline(steps=[
		('preprocessor', preprocessor),
		('model', model)
	])

	return my_pipeline


def evaluate_pipeline(pipeline, X_train, y_train, X_valid, y_valid):
	pipeline.fit(X_train, y_train)
	preds = pipeline.predict(X_valid)

	return mean_absolute_error(y_valid, preds)


def __get_preprocessor(numerical_cols, categorical_cols):
	# Preprocessing for numerical data
	numerical_transformer = Pipeline(steps=[
		('imputer', SimpleImputer(strategy='mean')),
		('scaler', StandardScaler())
	])

	# Preprocessing for categorical data
	categorical_transformer = Pipeline(steps=[
		('imputer', SimpleImputer(strategy='most_frequent')),
		('onehot', OneHotEncoder(handle_unknown='ignore'))
	])

	# Bundle preprocessing for numerical and categorical data
	preprocessor = ColumnTransformer(
		transformers=[
			('num', numerical_transformer, numerical_cols),
			('cat', categorical_transformer, categorical_cols),
		])

	return preprocessor
