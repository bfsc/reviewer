class CreateLancamentos < ActiveRecord::Migration
 def self.up
 create_table :lancamentos do |t|
 t.string :descricao, :null => false
 t.decimal :valor, :precision => 2, :null => false
 t.date :date, :null => false
 t.references :centro_de_custo

 t.timestamps
 end
 end

 def self.down
 drop_table :lancamentos
 end
end

