package com.vehicles.map.vehiclelist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vehicles.map.TestCoroutineRule
import com.vehicles.map.network.datasource.StubDataSource
import com.vehicles.map.utils.VehicleNetworkHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
internal class VehicleListViewModelTest {

    private lateinit var viewModel: VehicleListViewModel

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var networkHelper: VehicleNetworkHelper

    @Before
    fun setup() {
        viewModel = VehicleListViewModel(StubDataSource(), networkHelper)
    }

    @Test
    fun testVehicleList() {
        testCoroutineRule.runBlockingTest {
            viewModel.fetchVehicleList("","", "", "")
            val vehicles = viewModel.vehicleList
            Assert.assertNotNull(vehicles.value)
        }
    }

    @Test
    fun testClearViewModelData() {
        testCoroutineRule.runBlockingTest {
            viewModel.fetchVehicleList("","", "", "")
            val vehicles = viewModel.vehicleList
            viewModel.clearViewModelData()
            Assert.assertNull(vehicles.value)
        }
    }
}