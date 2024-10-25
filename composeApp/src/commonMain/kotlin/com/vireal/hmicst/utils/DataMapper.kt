package com.vireal.hmicst.utils

import com.vireal.hmicst.data.database.entities.CategoryEntity
import com.vireal.hmicst.data.database.entities.TransactionEntity
import com.vireal.hmicst.data.database.entities.UserEntity
import com.vireal.hmicst.data.models.CategoryModel
import com.vireal.hmicst.data.models.TransactionModel
import com.vireal.hmicst.data.models.UserModel

fun mapCategoryModelToCategoryEntity(categoryModel: CategoryModel): CategoryEntity =
    CategoryEntity(
        id = categoryModel.id,
        title = categoryModel.title,
        imageUrl = categoryModel.imageUrl,
    )

fun mapCategoryEntityToCategoryModel(categoryEntity: CategoryEntity): CategoryModel =
    CategoryModel(
        id = categoryEntity.id,
        title = categoryEntity.title,
        imageUrl = categoryEntity.imageUrl,
    )

fun mapUserModelToUserEntity(userModel: UserModel): UserEntity =
    UserEntity(
        id = userModel.id,
        yearlyNetIncome = userModel.yearlyNetIncome,
        recurrentSpendings = userModel.recurrentSpendings,
        savingsGoal = userModel.savingsGoal,
        startDate = userModel.startDate,
        dailyBalance = userModel.dailyBalance
    )

fun mapUserEntityToUserModel(userEntity: UserEntity): UserModel =
    UserModel(
        id = userEntity.id,
        yearlyNetIncome = userEntity.yearlyNetIncome,
        recurrentSpendings = userEntity.recurrentSpendings,
        savingsGoal = userEntity.savingsGoal,
        startDate = userEntity.startDate,
        dailyBalance = userEntity.dailyBalance
    )

fun mapTransactionModelToTransactionEntity(transactionModel: TransactionModel): TransactionEntity =
    TransactionEntity(
        date = transactionModel.date,
        amount = transactionModel.amount,
        categoryId = transactionModel.categoryId,
        userId = transactionModel.userId,
        title = transactionModel.title,
    )

fun mapTransactionEntityToTransactionModel(transactionEntity: TransactionEntity): TransactionModel =
    TransactionModel(
        id = transactionEntity.id,
        date = transactionEntity.date,
        amount = transactionEntity.amount,
        categoryId = transactionEntity.categoryId,
        userId = transactionEntity.userId,
        title = transactionEntity.title,
    )
