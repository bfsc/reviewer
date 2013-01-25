class Evento < ActiveRecord::Base
  attr_accessible :inicio, :local, :nome, :termino
end
