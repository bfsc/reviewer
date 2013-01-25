class CentroDeCustosController < ApplicationController
  # GET /centro_de_custos
  # GET /centro_de_custos.json
  def index
    @centro_de_custos = CentroDeCusto.all

    respond_to do |format|
      format.html # index.html.erb
      format.json { render json: @centro_de_custos }
    end
  end

  # GET /centro_de_custos/1
  # GET /centro_de_custos/1.json
  def show
    @centro_de_custo = CentroDeCusto.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.json { render json: @centro_de_custo }
    end
  end

  # GET /centro_de_custos/new
  # GET /centro_de_custos/new.json
  def new
    @centro_de_custo = CentroDeCusto.new

    respond_to do |format|
      format.html # new.html.erb
      format.json { render json: @centro_de_custo }
    end
  end

  # GET /centro_de_custos/1/edit
  def edit
    @centro_de_custo = CentroDeCusto.find(params[:id])
  end

  # POST /centro_de_custos
  # POST /centro_de_custos.json
  def create
    @centro_de_custo = CentroDeCusto.new(params[:centro_de_custo])

    respond_to do |format|
      if @centro_de_custo.save
        format.html { redirect_to @centro_de_custo, notice: 'Centro de custo was successfully created.' }
        format.json { render json: @centro_de_custo, status: :created, location: @centro_de_custo }
      else
        format.html { render action: "new" }
        format.json { render json: @centro_de_custo.errors, status: :unprocessable_entity }
      end
    end
  end

  # PUT /centro_de_custos/1
  # PUT /centro_de_custos/1.json
  def update
    @centro_de_custo = CentroDeCusto.find(params[:id])

    respond_to do |format|
      if @centro_de_custo.update_attributes(params[:centro_de_custo])
        format.html { redirect_to @centro_de_custo, notice: 'Centro de custo was successfully updated.' }
        format.json { head :no_content }
      else
        format.html { render action: "edit" }
        format.json { render json: @centro_de_custo.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /centro_de_custos/1
  # DELETE /centro_de_custos/1.json
  def destroy
    @centro_de_custo = CentroDeCusto.find(params[:id])
    @centro_de_custo.destroy

    respond_to do |format|
      format.html { redirect_to centro_de_custos_url }
      format.json { head :no_content }
    end
  end
end
