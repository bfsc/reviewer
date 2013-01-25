class EventosController < ApplicationController
  # GET /eventos
  # GET /eventos.json
  def index
    @eventos = Evento.all

    respond_to do |format|
      format.html # index.html.erb
      format.json { render json: @eventos }
    end
  end

  # GET /eventos/1
  # GET /eventos/1.json
  def show
    @evento = Evento.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.json { render json: @evento }
    end
  end

  # GET /eventos/new
  # GET /eventos/new.json
  def new
    @evento = Evento.new

    respond_to do |format|
      format.html # new.html.erb
      format.json { render json: @evento }
    end
  end

  # GET /eventos/1/edit
  def edit
    @evento = Evento.find(params[:id])
  end

  # POST /eventos
  # POST /eventos.json
  def create
    @evento = Evento.new(params[:evento])

    respond_to do |format|
      if @evento.save
        format.html { redirect_to @evento, notice: 'Evento was successfully created.' }
        format.json { render json: @evento, status: :created, location: @evento }
      else
        format.html { render action: "new" }
        format.json { render json: @evento.errors, status: :unprocessable_entity }
      end
    end
  end

  # PUT /eventos/1
  # PUT /eventos/1.json
  def update
    @evento = Evento.find(params[:id])

    respond_to do |format|
      if @evento.update_attributes(params[:evento])
        format.html { redirect_to @evento, notice: 'Evento was successfully updated.' }
        format.json { head :no_content }
      else
        format.html { render action: "edit" }
        format.json { render json: @evento.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /eventos/1
  # DELETE /eventos/1.json
  def destroy
    @evento = Evento.find(params[:id])
    @evento.destroy

    respond_to do |format|
      format.html { redirect_to eventos_url }
      format.json { head :no_content }
    end
  end
end
