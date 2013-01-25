class CreateLancamentos < ActiveRecord::Migration
  def change
    create_table :lancamentos do |t|
      t.string :descricao
      t.decimal :valor
      t.date :date
      t.references :centro_de_custo

      t.timestamps
    end
    add_index :lancamentos, :centro_de_custo_id
  end
end
