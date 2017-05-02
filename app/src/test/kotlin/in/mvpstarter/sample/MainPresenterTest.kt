package `in`.mvpstarter.sample

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

import `in`.mvpstarter.sample.common.TestDataFactory
import `in`.mvpstarter.sample.data.DataManager
import `in`.mvpstarter.sample.ui.main.MainMvpView
import `in`.mvpstarter.sample.ui.main.MainPresenter
import `in`.mvpstarter.sample.util.RxSchedulersOverrideRule
import io.reactivex.Single

import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

/**
 * Created by ravindra on 24/12/16.
 */
@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {

    @Mock
    internal var mMockMainMvpView: MainMvpView? = null
    @Mock
    internal var mMockDataManager: DataManager? = null
    private var mMainPresenter: MainPresenter? = null

    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Before
    fun setUp() {
        mMainPresenter = MainPresenter(mMockDataManager)
        mMainPresenter!!.attachView(mMockMainMvpView)
    }

    @After
    fun tearDown() {
        mMainPresenter!!.detachView()
    }

    @Test
    @Throws(Exception::class)
    fun getPokemonReturnsPokemonNames() {
        val pokemonList = TestDataFactory.makePokemonNamesList(10)
        `when`(mMockDataManager!!.getPokemonList(10))
                .thenReturn(Single.just(pokemonList))

        mMainPresenter!!.getPokemon(10)

        verify<MainMvpView>(mMockMainMvpView, times(2)).showProgress(anyBoolean())
        verify<MainMvpView>(mMockMainMvpView).showPokemon(pokemonList)
        verify<MainMvpView>(mMockMainMvpView, never()).showError(RuntimeException())

    }

    @Test
    @Throws(Exception::class)
    fun getPokemonReturnsError() {
        `when`(mMockDataManager!!.getPokemonList(10))
                .thenReturn(Single.error<List<String>>(RuntimeException()))

        mMainPresenter!!.getPokemon(10)

        verify<MainMvpView>(mMockMainMvpView, times(2)).showProgress(anyBoolean())
        verify<MainMvpView>(mMockMainMvpView).showError(any(Throwable::class.java))
        verify<MainMvpView>(mMockMainMvpView, never()).showPokemon(ArgumentMatchers.anyList<String>())
    }

}