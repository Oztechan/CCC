//
//  PremiumView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 21.08.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Res
import Provider
import SwiftUI

struct PremiumView: View {
    @Environment(\.colorScheme) private var colorScheme

    var event: PremiumEvent
    var state: PremiumState

    var body: some View {
        ZStack {
            Color(Res.colors().background_strong.get()).edgesIgnoringSafeArea(.all)

            VStack {
                Text(String(\.txt_premium))
                    .font(relative: .title2)
                    .padding(4.cp())
                    .padding(.top, 10.cp())

                if state.loading {
                    FormProgressView()
                } else {
                    Form {
                        List(state.premiumTypes, id: \.data) { premiumType in
                            PremiumItemView(item: premiumType)
                                .onTapGesture {
                                    event.onPremiumItemClick(type: premiumType)
                                }
                                .frame(minWidth: 0, maxWidth: .infinity, alignment: .center)
                        }
                        .listRowInsets(.init())
                        .listRowBackground(Res.colors().background.get())

                        PremiumItemView(item: nil)
                            .listRowInsets(.init())
                            .listRowBackground(Res.colors().background.get())
                    }
                    .withClearBackground(color: Res.colors().background.get())
                }

                Spacer()
            }.navigationBarHidden(true)
        }
    }
}
