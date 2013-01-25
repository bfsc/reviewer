class LancamentosController < ApplicationController
  # GET /lancamentos
  # GET /lancamentos.json
  def index
    @lancamentos = Lancamento.all

    respond_to do |format|
      format.html # index.html.erb
      format.json { render json: @lancamentos }
    end
  end

  # GET /lancamentos/1
  # GET /lancamentos/1.json
  def show
    @lancamento = Lancamento.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.json { render json: @lancamento }
    end
  end

  # GET /lancamentos/new
  # GET /lancamentos/new.json
  def new
    @lancamento = Lancamento.new

    respond_to do |format|
      format.html # new.html.erb
      format.json { render json: @lancamento }
    end
  end

  # GET /lancamentos/1/edit
  def edit
    @lancamento = Lancamento.find(params[:id])
  end

  # POST /lancamentos
  # POST /lancamentos.json
  def create
    @lancamento = Lancamento.new(params[:lancamento])

    respond_to do |format|
      if @lancamento.save
        format.html { redirect_to @lancamento, notice: 'Lancamento was successfully created.' }
        format.json { render json: @lancamento, status: :created, location: @lancamento }
      else
        format.html { render action: "new" }
        format.json { render json: @lancamento.errors, status: :unprocessable_entity }
      end
    end
  end

  # PUT /lancamentos/1
  # PUT /lancamentos/1.json
  def update
    @lancamento = Lancamento.find(params[:id])

    respond_to do |format|
      if @lancamento.update_attributes(params[:lancamento])
        format.html { redirect_to @lancamento, notice: 'Lancamento was successfully updated.' }
        format.json { head :no_content }
      else
        format.html { render action: "edit" }
        format.json { render json: @lancamento.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /lancamentos/1
  # DELETE /lancamentos/1.json
  def destroy
    @lancamento = Lancamento.find(params[:id])
    @lancamento.destroy

    respond_to do |format|
      format.html { redirect_to lancamentos_url }
      format.json { head :no_content }
    end
  end
end
