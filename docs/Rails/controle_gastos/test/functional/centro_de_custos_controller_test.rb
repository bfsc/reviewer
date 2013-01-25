require 'test_helper'

class CentroDeCustosControllerTest < ActionController::TestCase
  setup do
    @centro_de_custo = centro_de_custos(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:centro_de_custos)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create centro_de_custo" do
    assert_difference('CentroDeCusto.count') do
      post :create, centro_de_custo: { nome: @centro_de_custo.nome }
    end

    assert_redirected_to centro_de_custo_path(assigns(:centro_de_custo))
  end

  test "should show centro_de_custo" do
    get :show, id: @centro_de_custo
    assert_response :success
  end

  test "should get edit" do
    get :edit, id: @centro_de_custo
    assert_response :success
  end

  test "should update centro_de_custo" do
    put :update, id: @centro_de_custo, centro_de_custo: { nome: @centro_de_custo.nome }
    assert_redirected_to centro_de_custo_path(assigns(:centro_de_custo))
  end

  test "should destroy centro_de_custo" do
    assert_difference('CentroDeCusto.count', -1) do
      delete :destroy, id: @centro_de_custo
    end

    assert_redirected_to centro_de_custos_path
  end
end
