import traceback

import numpy as np
import pandas as pd
from sklearn.ensemble import RandomForestRegressor
from sklearn.model_selection import train_test_split

import constants
import data_investigation
import preprocessing

BASE_PATH = './resources'
PATH_TO_TEST_DATA = f'{BASE_PATH}/test.csv'
PATH_TO_TRAIN_DATA = f'{BASE_PATH}/train.csv'
LABEL = 'SalePrice'


def get_possible_features(houses_df: pd.DataFrame, exclude_thresholds=None):
    data_investigation.print_columns(houses_df)

    if exclude_thresholds is None:
        exclude_thresholds = np.arange(constants.X_THRESHOLDS_START, constants.X_THRESHOLDS_END,
                                       constants.X_THRESHOLDS_STEP)

    for exclude_threshold in exclude_thresholds:
        X_with_threshold = data_investigation.get_features(houses_df, exclude_threshold)
        yield X_with_threshold, exclude_threshold


def get_possible_random_forest_model(n_estimators=None):
    if n_estimators is None:
        n_estimators = range(constants.N_ESTIMATORS_START, constants.N_ESTIMATORS_END, constants.N_ESTIMATORS_STEP)

    for n_estimator in n_estimators:
        yield RandomForestRegressor(n_estimators=n_estimator, random_state=0), n_estimator


def get_best_params(X_train, X_valid, y_train, y_valid, verbose=False):
    """
    Try different params (feature threshold, random forest model estimators)
     and return params that get the minimum MAE error.
    """
    params = {
        'min_score': None,
        'pipeline': None
    }

    for X_with_threshold, exclude_threshold in get_possible_features(X_train):
        if verbose: print(f'{"_" * 20} threshold: {exclude_threshold} {"_" * 20}')
        for possible_model, n_estimator in get_possible_random_forest_model():
            if verbose: print()
            try:
                pipeline = preprocessing.get_pipeline(possible_model, X_with_threshold)
                mae_score = preprocessing.evaluate_pipeline(pipeline, X_with_threshold, y_train, X_valid, y_valid)

                if verbose: print(f'Score: {mae_score:,.3f} \t threshold: {exclude_threshold}\t n_estimator: {n_estimator}')
                if not params['min_score'] or params['min_score'] > mae_score:
                    params['min_score'] = mae_score
                    params['pipeline'] = pipeline
                    if verbose: print(f'> New min_score: {params["min_score"]}')
            except Exception:
                traceback.print_exc()

    return params


def get_pipeline_and_score(houses_df: pd.DataFrame, y, verbose=False):
    X_train, X_valid, y_train, y_valid = train_test_split(houses_df, y, train_size=0.8, test_size=0.2, random_state=1)
    params = get_best_params(X_train, X_valid, y_train, y_valid, verbose)

    return params['pipeline'], params['min_score']


def save_prediction_to_csv(houses_tests_df, label, pipeline, validation_min_score):
    preds_test = pipeline.predict(houses_tests_df)

    output = pd.DataFrame({'Id': houses_tests_df.Id, label: preds_test})
    default_filename = f'submission-{validation_min_score}-mean'
    filename = input(f'enter csv file name (default - "{default_filename}"): ') or default_filename
    output.to_csv(filename + '.csv', index=False)


def main():
    houses_df = pd.read_csv(PATH_TO_TRAIN_DATA)
    houses_tests_df = pd.read_csv(PATH_TO_TEST_DATA)
    y = houses_df[LABEL]
    houses_df = houses_df.drop(LABEL, axis=1)

    pipeline, validation_min_score = get_pipeline_and_score(houses_df, y, verbose=True)
    save_prediction_to_csv(houses_tests_df, LABEL, pipeline, validation_min_score)

    print('Done')


if __name__ == '__main__':
    main()
